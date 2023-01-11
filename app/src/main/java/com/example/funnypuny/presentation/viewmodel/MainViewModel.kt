package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.adapter.HorizontalCalendarAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(
    private val mainUseCase: MainUseCase
) : ViewModel() {

    private val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH).apply {
        add(Calendar.MONTH,6)
    }
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)

    // current date
    val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    // selected date
    var selectedDay: Int = currentDay
    var selectedMonth: Int = currentMonth
    var selectedYear: Int = currentYear

    // all days in month
    val dates = ArrayList<Date>()

    val monthTitleState = MutableLiveData<String>()
    val monthWithPositionState = MutableLiveData<Pair<Calendar?,Int>>()

    val habitListState =
        MutableLiveData<List<HabitEntity>>() //состояние вью, всегда мутабельная лайв дата
    //val showConfirmDeleteDialog = SingleLiveDataEmpty() //какое-то действие, которое нужно сделать один раз

    init {
        habitListState.value = mainUseCase.getHabitList()
       /* mainUseCase
            .habitsState()
            .subscribeOn()
            .observeOn()
            .subscribe{ habits->
                habitListState.value = habits
            }*/

        //lastDayInCalendar.add(Calendar.MONTH, 6)

        setUpCalendar(null)
    }

    //----------------


    fun onSwipeHabits(habit: HabitEntity) {
        mainUseCase.deleteHabitItem(habit)
        habitListState.value = mainUseCase.getHabitList()
        //habitListState.value = mainUseCase.deleteHabitItem(habit)
    }

    fun changeEnableState(habit: HabitEntity) {
        val newItem = habit.copy(enabled = !habit.enabled)
        //habitListState.value = mainUseCase.editHabitItem(newItem)
    }


    fun onPrevButtonClick() {
        if (cal.after(currentDate)) {
            cal.add(Calendar.MONTH, -1)
            if (cal == currentDate)
                setUpCalendar(null)
            else
                setUpCalendar(changeMonth = cal)
        }
    }

    fun onNextButtonClick() {
        if (cal.before(lastDayInCalendar)) {
            cal.add(Calendar.MONTH, 1)
            setUpCalendar(changeMonth = cal)
        }
    }

    fun onMonthClick(position: Int) {
        dates.getOrNull(position)?.let { date ->
            val clickCalendar = Calendar.getInstance()
            clickCalendar.time = date
            selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
        }
    }

    private fun setUpCalendar(changeMonth: Calendar?) {
        monthTitleState.value = sdf.format(cal.time)
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


        // При запуске приложение центрируется текущий день, если это не 1,2,3 и 29,30,31
        /*when {
            currentPosition > 2 -> binding.monthRecyclerView.scrollToPosition(currentPosition - 3)
            maxDaysInMonth - currentPosition < 2 -> binding.monthRecyclerView.scrollToPosition(
                currentPosition
            )
            else -> binding.monthRecyclerView.scrollToPosition(currentPosition)
        }*/

        val position = when {
            currentPosition > 2 -> currentPosition - 3
            maxDaysInMonth - currentPosition < 2 ->
                currentPosition
            else -> currentPosition
        }

        monthWithPositionState.value = Pair(changeMonth,position)

    }

}