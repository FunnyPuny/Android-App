package com.example.funnypuny.presentation.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        binding.ivMonthPrevButton.setOnClickListener { viewModel.onPrevMonthButtonClick() }
        //Переход к следуюющему месяцу
        binding.ivNextMonthButton.setOnClickListener { viewModel.onNextMonthButtonClick() }

        setupHabitList()
        setupBottomNavigation()

        viewModel.habitListState.observe(this) { habitListAdapter?.submitList(it) }
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

        viewModel.showHabitItemActivityEditItem.observe(this) {
            startActivity(HabitItemActivity.newIntentEditItem(this@MainActivity, it.id))
        }

        viewModel.showHabitItemFragmentEditItem.observe(this) {
            launchFragment(HabitItemFragment.newInstanceEditItem(it.id), true)
        }
    }

    override fun onHabitItemEditingFinished() {
        //todo через вьюмодель и SingleLiveEvent
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }

    private fun isOnePaneMode(): Boolean {
        //todo переименовать вcе xml идентификаторы по типу tv...
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
            onHabitItemLongClickListener = {
                //todo сделать по аналогии с  viewModel.onHabitAddClick(isOnePaneMode())
                //viewModel.onEditHabitItem(isOnePaneMode())
                if (isOnePaneMode()) {
                    Log.d("MainActivity", it.toString())
                    val intent = HabitItemActivity.newIntentEditItem(this@MainActivity, it.id)
                    startActivity(intent)
                } else {
                    launchFragment(HabitItemFragment.newInstanceEditItem(it.id), true)
                }
            }
            onHabitItemClickListener = { viewModel.changeEnableState(it) }
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

    private fun setupBottomNavigation() {
        with(binding.bnvMain) {
            itemIconTintList = null
            setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    //todo переименовать идентификаторы
                    when (item.itemId) {
                        R.id.nav_add_habit -> {
                            viewModel.onHabitAddClick(isOnePaneMode())
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.nav_home_page -> {
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.nav_statistic_page -> {
                            //todo сделать по аналогии с R.id.nav_add_box
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