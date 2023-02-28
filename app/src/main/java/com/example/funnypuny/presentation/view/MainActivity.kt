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
    private var horizontalCalendarAdapter: HorizontalCalendarAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivPrevMonthButton.setOnClickListener { viewModel.onPrevMonthButtonClick() }
        binding.ivNextMonthButton.setOnClickListener { viewModel.onNextMonthButtonClick() }

        setUpHorizontalCalendar()
        setupHabitList()
        setupBottomNavigation()

        viewModel.habitListState.observe(this) {
            habitListAdapter?.submitList(it)
        }

        viewModel.monthTitleState.observe(this) { binding.tvMonthTitle.text = it }

        viewModel.updateDatesAction.observe(this) {
            horizontalCalendarAdapter?.notifyDataSetChanged()
        }

        viewModel.scrollDatesToPositionAction.observe(this) { position ->
            binding.rvWeeklyCalendar.scrollToPosition(position)
        }

        viewModel.showHabitItemActivity.observe(this) { action ->
            startActivity(HabitItemActivity.newIntent(this@MainActivity, action))
        }

        viewModel.showHabitItemFragment.observe(this) { (action, withPopBackStack) ->
            launchFragment(HabitItemFragment.newInstance(action), withPopBackStack)
        }

        viewModel.showStatisticActivity.observe(this) {
            startActivity(StatisticsActivity.newIntent(this@MainActivity))
        }

        viewModel.showHabitItemEditingFinished.observe(this) {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onHabitItemEditingFinished() {
        viewModel.onHabitItemEditingFinished()
    }

    private fun isOnePaneMode(): Boolean {
        return binding.habitItemContainer == null
    }

    //todo поддержать сохранение активити из горизонтального в вертикальное
    private fun launchFragment(fragment: Fragment, withPopBackStack: Boolean) {
        if (withPopBackStack) supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.habitItemContainer, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun setUpHorizontalCalendar() {
        binding.rvWeeklyCalendar.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        horizontalCalendarAdapter = HorizontalCalendarAdapter(
            viewModel.dates,
            object : HorizontalCalendarAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    viewModel.onDayClick(position)
                }
            }
        )
        binding.rvWeeklyCalendar.adapter = horizontalCalendarAdapter
    }

    private fun setupHabitList() {
        habitListAdapter = HabitListAdapter().apply {
            onHabitItemLongClickListener = { habit ->
                viewModel.onEditHabitItem(isOnePaneMode(), habit.id)
            }
            onHabitItemClickListener = { habit ->
                viewModel.onChangeEnableState(habit)
            }
        }
        with(binding.rvHabitList) {
            adapter = habitListAdapter
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