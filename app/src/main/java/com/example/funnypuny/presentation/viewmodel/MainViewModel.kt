package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.HabitListSharedUseCase
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.adapter.HorizontalCalendarItem
import com.example.funnypuny.presentation.common.SingleLiveData
import com.example.funnypuny.presentation.common.SingleLiveDataEmpty
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.min

class MainViewModel(
    private val mainUseCase: MainUseCase, private val habitListSharedUseCase: HabitListSharedUseCase
) : ViewModel() {

    /*private val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH).apply {
        add(Calendar.MONTH, 6)
    }*/
    //private val monthSdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    //private val monthCalendar = Calendar.getInstance(Locale.ENGLISH)

    // current date
    //private var index = -1
    //private var selectCurrentDate = true

    private var selectedDate = Calendar.getInstance(Locale.ENGLISH).let { currentDateCalendar ->
        DateEntity(
            day = currentDateCalendar[Calendar.DAY_OF_MONTH],
            month = currentDateCalendar[Calendar.MONTH],
            year = currentDateCalendar[Calendar.YEAR]
        )
    }

    // all days in month
    val dates = mutableListOf<HorizontalCalendarItem>()

    val monthTitleState = MutableLiveData<String>()
    val monthWithPositionState = MutableLiveData<Pair<Calendar?, Int>>()

    val showHabitItemActivity = SingleLiveData<HabitActionEntity>()
    val showHabitItemFragment = SingleLiveData<Pair<HabitActionEntity, Boolean>>()

    //val showHabitItemActivityEditItem = SingleLiveData<HabitActionEntity>()

    val showStatisticActivity = SingleLiveDataEmpty()

    val showHabititemEditingFinished = SingleLiveDataEmpty()

    val updateDatesAction = SingleLiveDataEmpty()

    val habitListState = MutableLiveData<List<HabitEntity>>()


    init {
        habitListState.value = habitListSharedUseCase.getHabitList(selectedDate)
        setUpCalendar(0)
    }

    //----------------

    fun onViewShown() {
        habitListState.value = habitListSharedUseCase.getHabitList(selectedDate)
    }


    fun onSwipeHabit(position: Int) {
        habitListState.value?.getOrNull(position)?.let { habit ->
            habitListState.value = mainUseCase.deleteHabitItemState(selectedDate, habit)
        }
    }

    fun onChangeEnableState(habit: HabitEntity) {
        //val newItem = habit.copy(enabled = !habit.enabled)
        habitListState.value = mainUseCase.changeEnableHabitState(selectedDate, habit)
    }

    fun onPrevMonthButtonClick() {
        /*if (monthCalendar.after(currentDateCalendar)) {
            monthCalendar.add(Calendar.MONTH, -1)
            if (monthCalendar == currentDateCalendar) setUpCalendar(null)
            else setUpCalendar(changeMonth = monthCalendar)
        }*/
        //monthCalendar.add(Calendar.MONTH, -1)
        setUpCalendar(-1)
    }

    fun onNextMonthButtonClick() {
        /*if (monthCalendar.before(lastDayInCalendar)) {
            monthCalendar.add(Calendar.MONTH, 1)
            setUpCalendar(changeMonth = monthCalendar)
        }*/
        //monthCalendar.add(Calendar.MONTH, 1)
        setUpCalendar(1)
    }

    fun onDayClick(position: Int) {
        dates.find { it.isSelected }?.let { it.isSelected = false }
        dates.getOrNull(position)?.let { date ->
            //index = position
            //selectCurrentDate = false
            /*val clickCalendar = Calendar.getInstance()
            clickCalendar.time = date.dayOfTheMonth*/
            selectedDate = selectedDate.copy(day = date.dayOfTheMonth)
            date.isSelected = true
            updateDatesAction.call()
            habitListState.value = habitListSharedUseCase.getHabitList(selectedDate)
        }
    }

    fun onHabitAddClick(isPaneMode: Boolean) {
        if (isPaneMode) {
            showHabitItemActivity.value = HabitActionEntity.Add(selectedDate)
        } else {
            showHabitItemFragment.value = HabitActionEntity.Add(selectedDate) to true
        }
    }

    fun onStatisticPageClick() {
        showStatisticActivity.call()
    }

    fun onEditHabitItem(isPaneMode: Boolean, id: Int) {
        if (isPaneMode) {
            showHabitItemActivity.value = HabitActionEntity.Edit(selectedDate, id)
        } else {
            showHabitItemFragment.value = HabitActionEntity.Edit(selectedDate, id) to true
        }
    }

    fun onHabitItemEditingFinished() {
        showHabititemEditingFinished.call()
    }

    private fun setUpCalendar(deltaMonth: Int) {
        val monthSdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        val monthCalendar = Calendar.getInstance()
        monthCalendar.set(Calendar.DAY_OF_MONTH, selectedDate.day)
        monthCalendar.set(Calendar.MONTH, selectedDate.month)
        monthCalendar.set(Calendar.YEAR, selectedDate.year)
        monthCalendar.add(Calendar.MONTH, deltaMonth)
        //val monthCalendar = monthCalendar.clone() as Calendar
        val maxDaysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        //monthCalendar.set(Calendar.DAY_OF_MONTH, min(selectedDate.day, maxDaysInMonth))

        /*selectedDate = selectedDate.copy(
            day = when {
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            }, month = when {
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            }, year = when {
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            }
        )*/

        selectedDate = DateEntity(
            day = monthCalendar[Calendar.DAY_OF_MONTH],
            month = monthCalendar[Calendar.MONTH],
            year = monthCalendar[Calendar.YEAR]
        )
        dates.clear()
        val sdf = SimpleDateFormat("EEE", Locale.ENGLISH)
        for (i in 1..maxDaysInMonth) {
            monthCalendar.set(Calendar.DAY_OF_MONTH, i)
            dates.add(
                HorizontalCalendarItem(
                    isSelected = selectedDate.day == i,
                    dayOfTheMonth = i,
                    dayOfTheWeek = sdf.format(monthCalendar.time)
                )
            )
        }
        monthCalendar.set(Calendar.DAY_OF_MONTH, selectedDate.day)

        /*var currentPosition = 0
        var i = 0
        val sdf = SimpleDateFormat("EEE", Locale.ENGLISH)
        val cal = Calendar.getInstance()*/

        //monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        /*while (dates.size < maxDaysInMonth) {
            // get position of selected day
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDate.day) currentPosition =
                dates.size
            *//*dates.add(
                HorizontalCalendarItem(
                    monthCalendar.time,
                    isDateSelected(monthCalendar.time, i)
                )
            )*//*
            cal.time = monthCalendar.time
            dates.add(
                HorizontalCalendarItem(
                    isSelected = isDateSelected(monthCalendar.time, i),
                    dayOfTheMonth = cal[Calendar.DAY_OF_MONTH],
                    dayOfTheWeek = sdf.parse(cal.time.toString())
                        ?.let { sdf.format(it).toString() }
                        ?: ""
                )
            )
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
            i++
        }*/

        val selectedDayPosition = selectedDate.day - 1
        val centeredPosition = when {
            selectedDayPosition > 2 -> selectedDayPosition - 3
            maxDaysInMonth - selectedDayPosition < 2 -> selectedDayPosition
            else -> selectedDayPosition
        }

        monthTitleState.value = monthSdf.format(monthCalendar.time)
        monthWithPositionState.value = Pair(monthCalendar, centeredPosition)
        habitListState.value = habitListSharedUseCase.getHabitList(selectedDate)
    }

    /*private fun isDateSelected(date: Date, position: Int): Boolean {
        val cal = Calendar.getInstance()
        cal.time = date

        val displayMonth = cal[Calendar.MONTH]
        val displayYear = cal[Calendar.YEAR]
        val displayDay = cal[Calendar.DAY_OF_MONTH]

        if (displayYear >= currentYear) {
            return if (displayMonth >= currentMonth || displayYear > currentYear) {
                if (displayDay >= currentDay || displayMonth > currentMonth || displayYear > currentYear) {
                    if (index == position) {
                        true
                    } else {
                        displayDay == selectedDate.day && displayMonth == selectedDate.month && displayYear == selectedDate.year && selectCurrentDate
                    }
                } else {
                    false
                }
            } else {
                false
            }
        } else {
            return false
        }
    }*/
}
