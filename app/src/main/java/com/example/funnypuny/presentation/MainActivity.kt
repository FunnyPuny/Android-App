package com.example.funnypuny.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R
import com.example.funnypuny.databinding.ActivityMainBinding
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.presentation.adapter.HabitListAdapter
import com.example.funnypuny.presentation.adapter.HorizontalCalendarAdapter
import com.example.funnypuny.presentation.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), HabitItemFragment.OnHabitItemEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var habitListAdapter: HabitListAdapter
    private lateinit var horizontalCalendarAdapter: HorizontalCalendarAdapter

    private val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH)
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)

    // current date
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    // selected date
    private var selectedDay: Int = currentDay
    private var selectedMonth: Int = currentMonth
    private var selectedYear: Int = currentYear

    // all days in month
    private val dates = ArrayList<Date>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        HabitRepository.initialize(this)

        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.habitList.observe(this) {
            habitListAdapter.submitList(it)
        }

        binding.bottomNavigationMain.itemIconTintList = null

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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
                    //launchStatisticFragment(StatisticsFragment())
                    //launchFragment(StatisticsFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
        binding.bottomNavigationMain.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        /**
         * Adding SnapHelper here, but it is not needed. I add it just to looks better.
         */
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.monthRecyclerView)

        /**
         * This is the maximum month that the calendar will display.
         * I set it for 6 months, but you can increase or decrease as much you want.
         */
        lastDayInCalendar.add(Calendar.MONTH, 6)

        setUpCalendar()

        /**
         * Go to the previous month. First, make sure the current month (cal)
         * is after the current date so that you can't go before the current month.
         * Then subtract  one month from the sludge. Finally, ask if cal is equal to the current date.
         * If so, then you don't want to give @param changeMonth, otherwise changeMonth as cal.
         */
        binding.calendarPrevButton!!.setOnClickListener {
            if (cal.after(currentDate)) {
                cal.add(Calendar.MONTH, -1)
                if (cal == currentDate)
                    setUpCalendar()
                else
                    setUpCalendar(changeMonth = cal)
            }
        }

        /**
         * Go to the next month. First check if the current month (cal) is before lastDayInCalendar,
         * so that you can't go after the last possible month. Then add one month to cal.
         * Then put @param changeMonth.
         */
        binding.calendarNextButton!!.setOnClickListener {
            if (cal.before(lastDayInCalendar)) {
                cal.add(Calendar.MONTH, 1)
                setUpCalendar(changeMonth = cal)
            }
        }
    }

    private fun setUpCalendar(changeMonth: Calendar? = null) {
        binding.monthTextView!!.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        /**
         *
         * If changeMonth is not null, then I will take the day, month, and year from it,
         * otherwise set the selected date as the current date.
         */
        selectedDay =
            when {
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            }
        selectedMonth =
            when {
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            }
        selectedYear =
            when {
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            }

        var currentPosition = 0
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        /**
         * Fill dates with days and set currentPosition.
         * currentPosition is the position of first selected day.
         */
        while (dates.size < maxDaysInMonth) {
            // get position of selected day
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDay)
                currentPosition = dates.size
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Assigning calendar view.
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.monthRecyclerView!!.layoutManager = layoutManager
        val horizontalCalendarAdapter = HorizontalCalendarAdapter(this, dates, currentDate, changeMonth)
        binding.monthRecyclerView!!.adapter = horizontalCalendarAdapter

        /**
         * If you start the application, it centers the current day, but only if the current day
         * is not one of the first (1, 2, 3) or one of the last (29, 30, 31).
         */
        when {
            currentPosition > 2 -> binding.monthRecyclerView!!.scrollToPosition(currentPosition - 3)
            maxDaysInMonth - currentPosition < 2 -> binding.monthRecyclerView!!.scrollToPosition(currentPosition)
            else -> binding.monthRecyclerView!!.scrollToPosition(currentPosition)
        }


        /**
         * After calling up the OnClickListener, the text of the current month and year is changed.
         * Then change the selected day.
         */
        horizontalCalendarAdapter.setOnItemClickListener(object : HorizontalCalendarAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]
                selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
            }
        })
    }

    override fun onHabitItemEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }

    private fun isOnePaneMode(): Boolean {
        return binding.habitItemContainer == null
    }

    private fun launchStatisticFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .add(R.id.statistics_container, fragment)
            .addToBackStack(null)
            .commit()
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
                viewModel.deleteHabitItem(item)
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