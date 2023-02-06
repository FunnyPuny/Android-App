package com.example.funnypuny.presentation.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.funnypuny.R
import com.example.funnypuny.databinding.ActivityHabitItemBinding
import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.presentation.HabitItemFragment
import com.example.funnypuny.presentation.viewmodel.HabitItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HabitItemActivity : AppCompatActivity(),
    HabitItemFragment.OnHabitItemEditingFinishedListener {

    private val viewModel: HabitItemViewModel by viewModel(
        parameters = { parametersOf(getHabitAction(intent)) }
    )

    private var _binding: ActivityHabitItemBinding? = null
    private val binding: ActivityHabitItemBinding
        get() = _binding ?: throw RuntimeException("ActivityHabitItemBinding == null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHabitItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.showAction.observe(this) { action ->
            supportFragmentManager.beginTransaction()
                .replace(R.id.habitItemContainer, HabitItemFragment.newInstance(action))
                .commit()
        }


    }

    override fun onHabitItemEditingFinished() {
        finish()
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_HABIT_ITEM_ID = "extra_habit_item_id"

        private const val SELECTED_DAY = "selected_day"
        private const val SELECTED_MONTH = "selected_month"
        private const val SELECTED_YEAR = "selected_year"

        fun newIntent(context: Context, action: HabitActionEntity): Intent {
            val intent = Intent(context, HabitItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, HabitAction.ADD)
            when (action) {
                is HabitActionEntity.Add -> Unit
                is HabitActionEntity.Edit -> intent.putExtra(EXTRA_HABIT_ITEM_ID, action.id)
            }
            intent.putExtra(SELECTED_DAY, action.date.day)
            intent.putExtra(SELECTED_MONTH, action.date.month)
            intent.putExtra(SELECTED_YEAR, action.date.year)
            return intent
        }

        /*fun newIntentEditItem(context: Context, habitItemId: Int): Intent {
            val intent = Intent(context, HabitItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, HabitAction.EDIT)
            intent.putExtra(EXTRA_HABIT_ITEM_ID, habitItemId)
            return intent
        }*/

        fun getHabitAction(intent: Intent): HabitActionEntity {
            val action = intent.getSerializableExtra(EXTRA_SCREEN_MODE) as HabitAction
            val selectedDate = DateEntity(
                day = intent.getIntExtra(SELECTED_DAY, 0),
                month = intent.getIntExtra(SELECTED_MONTH, 0),
                year = intent.getIntExtra(SELECTED_YEAR, 0)
            )
            return when (action) {
                HabitAction.ADD -> HabitActionEntity.Add(selectedDate)
                HabitAction.EDIT -> HabitActionEntity.Edit(
                    date = selectedDate,
                    id = intent.getIntExtra(EXTRA_HABIT_ITEM_ID, HabitEntity.UNDEFINED_ID)
                )
            }
        }
    }
}

enum class HabitAction {
    ADD,
    EDIT
}