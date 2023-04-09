package com.example.funnypuny.presentation.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.funnypuny.R
import com.example.funnypuny.databinding.FragmentHabitItemBinding
import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.presentation.adapter.HabitFrequencyAdapter
import com.example.funnypuny.presentation.viewmodel.HabitItemFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HabitItemFragment : Fragment() {

    private var _binding: FragmentHabitItemBinding? = null
    private val binding: FragmentHabitItemBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitItemBinding == null")

    private lateinit var habitFrequencyAdapter: HabitFrequencyAdapter
    private lateinit var onHabitItemEditingFinishedListener: OnHabitItemEditingFinishedListener

    val viewModel: HabitItemFragmentViewModel by viewModel(parameters = {
        parametersOf(getHabitAction(requireArguments()))
    })


    override fun onAttach(context: Context) {
        Log.d("HabitItemFragment", "onAttach")
        super.onAttach(context)
        if (context is OnHabitItemEditingFinishedListener) {
            onHabitItemEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnHabitItemEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("HabitItemFragment", "onCreate")
        super.onCreate(savedInstanceState)
        //parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("HabitItemFragment", "onCreateView")
        _binding = FragmentHabitItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var progressDialog: Dialog? = null

    private val nameTextWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onNameChanged(s?.toString()?.trim())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("HabitItemFragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        binding.tietName.addTextChangedListener(nameTextWatcher)

        /*binding.tietName.setText(viewModel.inputName)
        binding.tietName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onNameChanged(s?.toString()?.trim())
            }
        })*/

        binding.btnSave.setOnClickListener {
            viewModel.onSaveClick()
        }

        viewModel.daysOfTheWeekState.observe(viewLifecycleOwner) {
            habitFrequencyAdapter = HabitFrequencyAdapter(it)
            binding.rvFrequencyOfTheDay.adapter = habitFrequencyAdapter
        }

        viewModel.shouldCloseScreenState.observe(viewLifecycleOwner) {
            onHabitItemEditingFinishedListener.onHabitItemEditingFinished()
            //activity?.onBackPressed()
        }

        viewModel.errorInputNameState.observe(viewLifecycleOwner) { isError ->
            val message = if (isError) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tietName.error = message
        }

        viewModel.showErrorToast.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }

        viewModel.initInputName.observe(viewLifecycleOwner) { habit ->
            binding.tietName.removeTextChangedListener(nameTextWatcher)
            binding.tietName.setText(habit)
            binding.tietName.addTextChangedListener(nameTextWatcher)
        }

        viewModel.progressVisibilityState.observe(viewLifecycleOwner) { isVisible ->
            progressDialog?.dismiss()
            if (isVisible) {
                progressDialog = Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar)
                    .apply {
                        setContentView(
                            layoutInflater.inflate(
                                R.layout.fullscreen_progressbar_dialog_fragment,
                                null
                            )
                        )
                        setCancelable(false)
                        show()
                    }
            }
        }

    }

    override fun onStart() {
        Log.d("HabitItemFragment", "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d("HabitItemFragment", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("HabitItemFragment", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("HabitItemFragment", "onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("HabitItemFragment", "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        Log.d("HabitItemFragment", "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("HabitItemFragment", "onDetach")
        super.onDetach()
    }

    interface OnHabitItemEditingFinishedListener {

        fun onHabitItemEditingFinished()
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_screen_mode"
        private const val HABIT_ITEM_ID = "habit_item_id"
        private const val SELECTED_DAY = "selected_day"
        private const val SELECTED_MONTH = "selected_month"
        private const val SELECTED_YEAR = "selected_year"

        fun newInstance(action: HabitActionEntity): HabitItemFragment {
            return HabitItemFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_SCREEN_MODE, HabitAction.EDIT)
                    when (action) {
                        is HabitActionEntity.Add -> putSerializable(
                            EXTRA_SCREEN_MODE,
                            HabitAction.ADD
                        )
                        is HabitActionEntity.Edit -> putInt(HABIT_ITEM_ID, action.id)
                    }
                    putInt(SELECTED_DAY, action.date.day)
                    putInt(SELECTED_MONTH, action.date.month)
                    putInt(SELECTED_YEAR, action.date.year)
                }
            }
        }

        fun getHabitAction(bundle: Bundle): HabitActionEntity {
            val action = (bundle.getSerializable(EXTRA_SCREEN_MODE) as? HabitAction)!!
            val selectedDate = DateEntity(
                day = bundle.getInt(SELECTED_DAY, 0),
                month = bundle.getInt(SELECTED_MONTH, 0),
                year = bundle.getInt(SELECTED_YEAR, 0)
            )
            return when (action) {
                HabitAction.ADD -> HabitActionEntity.Add(selectedDate)
                HabitAction.EDIT -> HabitActionEntity.Edit(
                    date = selectedDate,
                    id = bundle.getInt(HABIT_ITEM_ID, HabitEntity.UNDEFINED_ID)
                )
            }
        }
    }
}