package com.example.funnypuny.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.funnypuny.databinding.FragmentHabitItemBinding
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.presentation.adapter.FrequencyOfTheDayAdapter
import com.example.funnypuny.presentation.viewmodel.HabitItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HabitItemFragment: Fragment() {

    private var _binding: FragmentHabitItemBinding? = null
    private val binding: FragmentHabitItemBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitItemBinding == null")

    private lateinit var frequencyOfTheDayAdapter: FrequencyOfTheDayAdapter
    private lateinit var onHabitItemEditingFinishedListener: OnHabitItemEditingFinishedListener

    val viewModel: HabitItemViewModel by viewModel()
    private var screenMode = MODE_UNKNOWN
    private var habitId: Int = HabitEntity.UNDEFINED_ID


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
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HabitItemFragment", "onCreateView")
        _binding = FragmentHabitItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("HabitItemFragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(this)[HabitItemViewModel::class.java]
        //binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        //initViews(view)
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()

        val data = ArrayList<HabitFrequencyEntity>()

        data.add(HabitFrequencyEntity("Sun"))
        data.add(HabitFrequencyEntity("Mon"))
        data.add(HabitFrequencyEntity("Tue"))
        data.add(HabitFrequencyEntity("Wed"))
        data.add(HabitFrequencyEntity("Thu"))
        data.add(HabitFrequencyEntity("Fri"))
        data.add(HabitFrequencyEntity("Sat"))

        val rvFrequencyList = binding.rvFrequencyOfTheDay
        frequencyOfTheDayAdapter = FrequencyOfTheDayAdapter(data)
        rvFrequencyList.adapter = frequencyOfTheDayAdapter

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

    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onHabitItemEditingFinishedListener.onHabitItemEditingFinished()
            //activity?.onBackPressed()
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangeListeners() {
        binding.tietName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun launchEditMode() {
        viewModel.getHabitItem(habitId)
        binding.btnSave.setOnClickListener {
            viewModel.editHabitItem(binding.tietName.text?.toString())
        }
    }

    private fun launchAddMode() {
        binding.btnSave.setOnClickListener {
            viewModel.addHabitItem(binding.tietName.text?.toString())
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(HABIT_ITEM_ID)) {
                throw RuntimeException("Param habit item id is absent")
            }
            habitId = args.getInt(HABIT_ITEM_ID, HabitEntity.UNDEFINED_ID)
        }
    }

    /*private fun initViews(view: View) {
        with(view) {
            etName = findViewById(R.id.et_name)
            buttonSave = findViewById(R.id.save_button)
        }
    }*/

    interface OnHabitItemEditingFinishedListener {

        fun onHabitItemEditingFinished()
    }

    companion object {

        private const val SCREEN_MODE = "screen_mode"
        private const val HABIT_ITEM_ID = "habit_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): HabitItemFragment {
            return HabitItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(habitItemId: Int): HabitItemFragment {
            return HabitItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(HABIT_ITEM_ID, habitItemId)
                }
            }
        }
    }
}