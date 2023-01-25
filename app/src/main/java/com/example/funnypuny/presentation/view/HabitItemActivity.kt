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
import com.example.funnypuny.presentation.viewmodel.HabitItemAction
import com.example.funnypuny.presentation.viewmodel.HabitItemViewModel
import com.example.funnypuny.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HabitItemActivity : AppCompatActivity(),
    HabitItemFragment.OnHabitItemEditingFinishedListener {

    private val viewModel: HabitItemViewModel by viewModel()

    private var _binding: ActivityHabitItemBinding? = null
    private val binding: ActivityHabitItemBinding
        get() = _binding ?: throw RuntimeException("ActivityHabitItemBinding == null")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHabitItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mode = intent.getSerializableExtra(EXTRA_SCREEN_MODE) as HabitItemAction
        val habitId = intent.getIntExtra(EXTRA_HABIT_ITEM_ID, HabitEntity.UNDEFINED_ID)
        viewModel.init(mode,habitId)

        viewModel.showNewInstanceAddItem.observe(this){
            supportFragmentManager.beginTransaction()
                .replace(R.id.habit_item_container, HabitItemFragment.newInstanceAddItem())
                .commit()
        }

        viewModel.showNewInstanceEditItem.observe(this){
            supportFragmentManager.beginTransaction()
                .replace(R.id.habit_item_container, HabitItemFragment.newInstanceEditItem(habitId))
                .commit()
        }


    }

    override fun onHabitItemEditingFinished() {
        finish()
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_HABIT_ITEM_ID = "extra_habit_item_id"

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, HabitItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, HabitItemAction.ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, habitItemId: Int): Intent {
            val intent = Intent(context, HabitItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, HabitItemAction.EDIT)
            intent.putExtra(EXTRA_HABIT_ITEM_ID, habitItemId)
            return intent
        }
    }
}