package com.example.funnypuny.presentation.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R
import com.example.funnypuny.databinding.ActivityMainBinding
import com.example.funnypuny.presentation.HabitItemActivity
import com.example.funnypuny.presentation.HabitItemFragment
import com.example.funnypuny.presentation.StatisticsActivity
import com.example.funnypuny.presentation.adapter.HabitListAdapter
import com.example.funnypuny.presentation.adapter.HorizontalCalendarAdapter
import com.example.funnypuny.presentation.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar


class MainActivity : AppCompatActivity(), HabitItemFragment.OnHabitItemEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModel()

    private lateinit var habitListAdapter: HabitListAdapter
    private lateinit var horizontalCalendarAdapter: HorizontalCalendarAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        viewModel.habitListState.observe(this) { habitListAdapter.submitList(it) }
        viewModel.monthTitleState.observe(this) { binding.monthTextView.text = it }
        viewModel.monthWithPositionState.observe(this) { (changeMonth,position) ->
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.monthRecyclerView.layoutManager = layoutManager
            val horizontalCalendarAdapter =
                HorizontalCalendarAdapter(this, viewModel.dates, viewModel.currentDate, changeMonth)
            binding.monthRecyclerView.adapter = horizontalCalendarAdapter
            binding.monthRecyclerView.scrollToPosition(position)
            horizontalCalendarAdapter.setOnItemClickListener(object :
                HorizontalCalendarAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    viewModel.onMonthClick(position)
                }
            })
        }

        binding.bottomNavigationMain.itemIconTintList = null

        val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_add_box -> {
                        if (isOnePaneMode()) {
                            val intent = HabitItemActivity.newIntentAddItem(this)
                            startActivity(intent)
                        } else {
                            launchFragment(HabitItemFragment.newInstanceAddItem())
                        }
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.nav_home -> {
                        // put your code here
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.nav_profile -> {
                        val intent = StatisticsActivity.newIntent(this)
                        startActivity(intent)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }
        binding.bottomNavigationMain.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        //Переход к предедущему месяцу
        binding.calendarPrevButton?.setOnClickListener {
            viewModel.onPrevButtonClick()
        }

        //Переход к следуюющему месяцу
        binding.calendarNextButton?.setOnClickListener {
            viewModel.onNextButtonClick()
        }
    }

    override fun onHabitItemEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }

    private fun isOnePaneMode(): Boolean {
        return binding.habitItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.habit_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun setupRecyclerView() {
        val rvShopList = binding.rvHabitList
        with(rvShopList) {
            habitListAdapter = HabitListAdapter()
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
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
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
                val item = habitListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.onSwipeHabits(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupLongClickListener() {
        habitListAdapter.onHabitItemLongClickListener = {
            if (isOnePaneMode()) {
                Log.d("MainActivity", it.toString())
                val intent = HabitItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(HabitItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupClickListener() {
        habitListAdapter.onHabitItemClickListener = {
            viewModel.changeEnableState(it)
        }
    }

}