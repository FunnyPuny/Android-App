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
import com.example.funnypuny.presentation.viewmodel.HorizontalCalendarViewModel
import com.example.funnypuny.presentation.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity(), HabitItemFragment.OnHabitItemEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModel()

    private lateinit var calendaViewModel: HorizontalCalendarViewModel

    private lateinit var habitListAdapter: HabitListAdapter
    private lateinit var horizontalCalendarAdapter: HorizontalCalendarAdapter

    /*private val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH)
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
    private val dates = ArrayList<Date>()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        viewModel.habitListState.observe(this) { habitListAdapter.submitList(it) }

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

        /**
         * Adding SnapHelper here, but it is not needed. I add it just to looks better.
         */
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.monthRecyclerView)

        // Максимальное количество месяцев, которое отображается в календаре
        calendaViewModel.lastDayInCalendar.add(Calendar.MONTH, 6)

        setUpCalendar()

        //Переход к предедущему месяцу
        binding.calendarPrevButton!!.setOnClickListener {
            /*if (cal.after(currentDate)) {
                cal.add(Calendar.MONTH, -1)
                if (cal == currentDate)
                    setUpCalendar()
                else
                    setUpCalendar(changeMonth = cal)
            }*/
            calendaViewModel.onPrevButtonClick()
        }

        //Переход к следуюющему месяцу
        binding.calendarNextButton!!.setOnClickListener {
            /*if (cal.before(lastDayInCalendar)) {
                cal.add(Calendar.MONTH, 1)
                setUpCalendar(changeMonth = cal)
            }*/
            calendaViewModel.onNextButtonClick()
        }
    }

    private fun setUpCalendar(changeMonth: Calendar? = null) {
        binding.monthTextView.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        //Если changeMonth не равен нулю, то я возьму из него день, месяц и год,
        //в противном случае установите выбранную дату как текущую дату.
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

        //Заполнение даты днями и установить currentPosition.
        //currentPosition — позиция первого выбранного дня.
        while (dates.size < maxDaysInMonth) {
            // get position of selected day
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDay)
                currentPosition = dates.size
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Assigning calendar view.
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.monthRecyclerView.layoutManager = layoutManager
        val horizontalCalendarAdapter =
            HorizontalCalendarAdapter(this, dates, currentDate, changeMonth)
        binding.monthRecyclerView.adapter = horizontalCalendarAdapter

        // При запуске приложение центрируется текущий день, если это не 1,2,3 и 29,30,31
        when {
            currentPosition > 2 -> binding.monthRecyclerView.scrollToPosition(currentPosition - 3)
            maxDaysInMonth - currentPosition < 2 -> binding.monthRecyclerView.scrollToPosition(
                currentPosition
            )
            else -> binding.monthRecyclerView.scrollToPosition(currentPosition)
        }


        //Меняется текст текущего месяца и года
        horizontalCalendarAdapter.setOnItemClickListener(object :
            HorizontalCalendarAdapter.OnItemClickListener {
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