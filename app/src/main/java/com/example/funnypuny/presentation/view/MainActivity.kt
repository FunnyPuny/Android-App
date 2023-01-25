package com.example.funnypuny.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R
import com.example.funnypuny.databinding.ActivityMainBinding
import com.example.funnypuny.presentation.HabitItemFragment
import com.example.funnypuny.presentation.adapter.HabitListAdapter
import com.example.funnypuny.presentation.adapter.HorizontalCalendarAdapter
import com.example.funnypuny.presentation.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), HabitItemFragment.OnHabitItemEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModel()

    private var habitListAdapter: HabitListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Переход к предедущему месяцу
        binding.ivPrevMonthButton.setOnClickListener { viewModel.onPrevMonthButtonClick() }
        //Переход к следуюющему месяцу
        binding.ivNextMonthButton.setOnClickListener { viewModel.onNextMonthButtonClick() }

        setupHabitList()
        setupBottomNavigation()

        //todo при добавлении элемент не появился
        viewModel.habitListState.observe(this) {
            habitListAdapter?.submitList(it)
            //todo надо убрать
            habitListAdapter?.notifyDataSetChanged()
        }
        viewModel.monthTitleState.observe(this) { binding.tvMonthTitle.text = it }
        viewModel.monthWithPositionState.observe(this) { (changeMonth, position) ->
            //todo сделать инициализацию адаптера один раз, а здесь просто уведомляь его о изменениях
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.rvWeeklyCalendar.layoutManager = layoutManager
            val horizontalCalendarAdapter =
                HorizontalCalendarAdapter(this, viewModel.dates, viewModel.currentDate, changeMonth)
            binding.rvWeeklyCalendar.adapter = horizontalCalendarAdapter
            binding.rvWeeklyCalendar.scrollToPosition(position)
            horizontalCalendarAdapter.setOnItemClickListener(object :
                HorizontalCalendarAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    viewModel.onMonthClick(position)
                }
            })
        }
        viewModel.showHabitItemActivity.observe(this) {
            startActivity(HabitItemActivity.newIntentAddItem(this))
        }

        viewModel.showHabitItemFragment.observe(this) { withPopBackStack ->
            launchFragment(HabitItemFragment.newInstanceAddItem(), withPopBackStack)
        }

        viewModel.showStatisticActivity.observe(this) {
            startActivity(StatisticsActivity.newIntent(this@MainActivity))
        }

        viewModel.showHabitItemActivityEditItem.observe(this) { id ->
            startActivity(HabitItemActivity.newIntentEditItem(this@MainActivity, id))
        }

        viewModel.showHabitItemFragmentEditItem.observe(this) { (id, withPopBackStack) ->
            launchFragment(HabitItemFragment.newInstanceEditItem(id), withPopBackStack)
        }

        viewModel.showHabititemEditingFinished.observe(this) {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onHabitItemEditingFinished() {
        viewModel.onHabitItemEditingFinished()
    }

    private fun isOnePaneMode(): Boolean {
        return binding.habitItemContainer == null
    }

    private fun launchFragment(fragment: Fragment, withPopBackStack: Boolean) {
        if (withPopBackStack) supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.habit_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun setupHabitList() {
        habitListAdapter = HabitListAdapter().apply {
            onHabitItemLongClickListener = { habit ->
                viewModel.onEditHabitItem(isOnePaneMode(),habit.id)
            }
            onHabitItemClickListener = { habit ->
                viewModel.changeEnableState(habit) }
        }
        with(binding.rvHabitList) {
            adapter = habitListAdapter
            recycledViewPool.setMaxRecycledViews(
                HabitListAdapter.VIEW_TYPE_ENABLED,
                HabitListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                HabitListAdapter.VIEW_TYPE_DISABLED,
                HabitListAdapter.MAX_POOL_SIZE
            )
        }
        setupSwipeHabitListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onViewShown()
    }

    private fun setupBottomNavigation() {
        with(binding.bnvMain) {
            itemIconTintList = null
            setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.nav_add_habit -> {
                            viewModel.onHabitAddClick(isOnePaneMode())
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.nav_home_page -> {
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.nav_statistic_page -> {
                            viewModel.onStatisticPageClick()
                            return@OnNavigationItemSelectedListener true
                        }
                    }
                    false
                }
            )
        }
    }


    private fun setupSwipeHabitListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.onSwipeHabit(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvHabitList)
    }

}