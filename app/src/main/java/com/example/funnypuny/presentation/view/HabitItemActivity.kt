package com.example.funnypuny.presentation.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.funnypuny.R
import com.example.funnypuny.databinding.ActivityHabitItemBinding
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.presentation.HabitItemFragment
import com.example.funnypuny.presentation.adapter.HabitFrequencyAdapter
import com.example.funnypuny.presentation.viewmodel.HabitItemViewModel
import com.example.funnypuny.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HabitItemActivity : AppCompatActivity(),
    HabitItemFragment.OnHabitItemEditingFinishedListener {

    val viewModel: HabitItemViewModel by viewModel()

    private var _binding: ActivityHabitItemBinding? = null
    private val binding: ActivityHabitItemBinding
        get() = _binding ?: throw RuntimeException("ActivityHabitItemBinding == null")


    private var screenMode = MODE_UNKNOWN
    private var habitId = HabitEntity.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHabitItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        launchRightMode()

        viewModel.showEditHabitItemFragment.observe(this){
            HabitItemFragment.newInstanceEditItem(habitId)
        }
        viewModel.showAddHabitItemFragment.observe(this){
            HabitItemFragment.newInstanceAddItem()
        }

        viewModel.showException.observe(this){
            throw RuntimeException(it)
        }

    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> viewModel.onEditHabitItemFragment()
            MODE_ADD -> viewModel.onAddHabitItemFragment()
            else -> viewModel.onShowRuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.habit_item_container, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            viewModel.onShowRuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            //viewModel.onShowRuntimeException("Unknown screen mode $mode")
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_HABIT_ITEM_ID)) {
                viewModel.onShowRuntimeException("Param shop item id is absent")
            }
            habitId = intent.getIntExtra(EXTRA_HABIT_ITEM_ID, HabitEntity.UNDEFINED_ID)
        }
    }

    override fun onHabitItemEditingFinished() {
        finish()
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_HABIT_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, HabitItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, habitItemId: Int): Intent {
            val intent = Intent(context, HabitItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_HABIT_ITEM_ID, habitItemId)
            return intent
        }
    }
}