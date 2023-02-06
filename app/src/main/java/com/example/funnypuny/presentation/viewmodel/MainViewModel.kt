package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.HabitListSharedUseCase
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.common.SingleLiveData
import com.example.funnypuny.presentation.common.SingleLiveDataEmpty
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel(
    private val mainUseCase: MainUseCase,
    private val habitListSharedUseCase: HabitListSharedUseCase
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

    var selectedDate = DateEntity(currentDay,currentMonth,currentYear)

    // all days in month
    val dates = ArrayList<Date>()

    val monthTitleState = MutableLiveData<String>()
    val monthWithPositionState = MutableLiveData<Pair<Calendar?, Int>>()

    val showHabitItemActivity = SingleLiveData<HabitActionEntity>()
    val showHabitItemFragment = SingleLiveData<Pair<HabitActionEntity, Boolean>>()

    val showHabitItemActivityEditItem = SingleLiveData<Int>()

    val showStatisticActivity = SingleLiveDataEmpty()

    val showHabititemEditingFinished = SingleLiveDataEmpty()

    val habitListState = MutableLiveData<List<HabitEntity>>()


    init {
        habitListState.value = habitListSharedUseCase.getHabitList(selectedDate)
        setUpCalendar(null)
    }

    //----------------

    fun onViewShown() {
        habitListState.value = habitListSharedUseCase.getHabitList(selectedDate)
    }


    fun onSwipeHabit(position: Int) {
        habitListState.value?.getOrNull(position)?.let { habit ->
            habitListState.value = mainUseCase.deleteHabitItemState(selectedDate,habit)
        }
    }

    fun onChangeEnableState(habit: HabitEntity) {
        //val newItem = habit.copy(enabled = !habit.enabled)
        habitListState.value = mainUseCase.changeEnableHabitState(selectedDate,habit)
    }

    fun onPrevMonthButtonClick() {
        if (cal.after(currentDate)) {
            cal.add(Calendar.MONTH, -1)
            if (cal == currentDate)
                setUpCalendar(null)
            else
                setUpCalendar(changeMonth = cal)
        }
    }

    fun onNextMonthButtonClick() {
        if (cal.before(lastDayInCalendar)) {
            cal.add(Calendar.MONTH, 1)
            setUpCalendar(changeMonth = cal)
        }
    }

    fun onDayClick(position: Int) {
        dates.getOrNull(position)?.let { date ->
            val clickCalendar = Calendar.getInstance()
            clickCalendar.time = date
            //selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
            selectedDate = selectedDate.copy(day = clickCalendar[Calendar.DAY_OF_MONTH])
            habitListState.value = habitListSharedUseCase.getHabitList(selectedDate)
        }
    }

    fun onHabitAddClick(isPaneMode:Boolean){
        if (isPaneMode) {
            showHabitItemActivity.value = HabitActionEntity.Add(selectedDate)
        } else {
            showHabitItemFragment.value = HabitActionEntity.Add(selectedDate) to false
        }
    }

    fun onStatisticPageClick() {
        showStatisticActivity.call()
    }

    fun onEditHabitItem(isPaneMode: Boolean, id: Int) {
        if (isPaneMode) {
            showHabitItemActivityEditItem.value = id
        } else {
            showHabitItemFragment.value = HabitActionEntity.Edit(selectedDate, id) to true
        }
    }

    fun onHabitItemEditingFinished() {
        showHabititemEditingFinished.call()
    }

    private fun setUpCalendar(changeMonth: Calendar?) {
        monthTitleState.value = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        selectedDate = selectedDate.copy(
            day = when {
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            },
            month = when {
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            },
            year = when {
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            } )
        habitListState.value = habitListSharedUseCase.getHabitList(selectedDate)

        var currentPosition = 0
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        while (dates.size < maxDaysInMonth) {
            // get position of selected day
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDate.day)
                currentPosition = dates.size
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val position = when {
            currentPosition > 2 -> currentPosition - 3
            maxDaysInMonth - currentPosition < 2 ->
                currentPosition
            else -> currentPosition
        }

        monthWithPositionState.value = Pair(changeMonth, position)

    }

}