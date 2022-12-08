package com.example.funnypuny.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.funnypuny.R
import com.example.funnypuny.domain.HabitItem
import com.google.android.material.textfield.TextInputLayout

class HabitItemFragment: Fragment() {

    private lateinit var onHabitItemEditingFinishedListener: OnHabitItemEditingFinishedListener

    private lateinit var viewModel: HabitItemViewModel

    private lateinit var etName: EditText
    private lateinit var buttonSave: Button

    private var screenMode = MODE_UNKNOWN
    private var habitItemId: Int = HabitItem.UNDEFINED_ID

    /*override fun onAttach(context: Context) {
        Log.d("ShopItemFragment", "onAttach")
        super.onAttach(context)
        if (context is OnHabitItemEditingFinishedListener) {
            onHabitItemEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnHabitItemEditingFinishedListener")
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("HabitItemFragment", "onCreate")
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ShopItemFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_habit_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("HabitItemFragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HabitItemViewModel::class.java]
        initViews(view)
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
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
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            etName.error = message
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            //onHabitItemEditingFinishedListener.onHabitItemEditingFinished()
            activity?.onBackPressed()
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
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
        viewModel.getHabitItem(habitItemId)
        viewModel.habitItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
        }
        buttonSave.setOnClickListener {
            viewModel.editHabitItem(etName.text?.toString())
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addHabitItem(etName.text?.toString())
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
                throw RuntimeException("Param shop item id is absent")
            }
            habitItemId = args.getInt(HABIT_ITEM_ID, HabitItem.UNDEFINED_ID)
        }
    }

    private fun initViews(view: View) {
        with(view) {
            etName = findViewById(R.id.et_name)
            buttonSave = findViewById(R.id.save_button)
        }
    }

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