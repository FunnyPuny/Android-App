package com.example.funnypuny.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.funnypuny.R
import com.example.funnypuny.databinding.FragmentHabitItemBinding
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.presentation.adapter.HabitFrequencyAdapter
import com.example.funnypuny.presentation.view.HabitAction
import com.example.funnypuny.presentation.view.HabitItemActivity
import com.example.funnypuny.presentation.viewmodel.HabitItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HabitItemFragment : Fragment() {

    private var _binding: FragmentHabitItemBinding? = null
    private val binding: FragmentHabitItemBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitItemBinding == null")

    private lateinit var habitFrequencyAdapter: HabitFrequencyAdapter
    private lateinit var onHabitItemEditingFinishedListener: OnHabitItemEditingFinishedListener

    val viewModel: HabitItemViewModel by viewModel(parameters = {
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

        //слушатель ввода текста
        binding.tietName.addTextChangedListener(nameTextWatcher)

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

        viewModel.habitState.observe(viewLifecycleOwner) { habit ->
            binding.tietName.removeTextChangedListener(nameTextWatcher)
            binding.tietName.setText(habit.name)
            binding.tietName.addTextChangedListener(nameTextWatcher)
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

        fun newInstanceAddItem(): HabitItemFragment {
            return HabitItemFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_SCREEN_MODE, HabitAction.ADD)
                }
            }
        }

        fun newInstanceEditItem(habitItemId: Int): HabitItemFragment {
            return HabitItemFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_SCREEN_MODE, HabitAction.EDIT)
                    putInt(HABIT_ITEM_ID, habitItemId)
                }
            }
        }

        fun getHabitAction(bundle: Bundle): HabitActionEntity {
            //val args = requireArguments()
            val action = (bundle.getSerializable(EXTRA_SCREEN_MODE) as? HabitAction)!!
            val id = bundle.getInt(HABIT_ITEM_ID, HabitEntity.UNDEFINED_ID)
            return when (action) {
                HabitAction.ADD -> HabitActionEntity.Add
                HabitAction.EDIT -> HabitActionEntity.Edit(id)
            }
        }
    }
}