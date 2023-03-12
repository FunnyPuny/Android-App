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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel(
    private val mainUseCase: MainUseCase, private val habitListSharedUseCase: HabitListSharedUseCase
) : ViewModel() {

    private var selectedDate = Calendar.getInstance(Locale.ENGLISH).let { currentDateCalendar ->
        DateEntity(
            day = currentDateCalendar[Calendar.DAY_OF_MONTH],
            month = currentDateCalendar[Calendar.MONTH],
            year = currentDateCalendar[Calendar.YEAR]
        )
    }

    val dates = mutableListOf<HorizontalCalendarItem>()
    var scrollDayPosition: Int? = null

    val habitListState = MutableLiveData<List<HabitEntity>>()
    val monthTitleState = MutableLiveData<String>()
    val scrollDatesToPositionAction = SingleLiveData<Int>()
    val showHabitItemActivity = SingleLiveData<HabitActionEntity>()
    val showHabitItemFragment = SingleLiveData<Pair<HabitActionEntity, Boolean>>()
    val showStatisticActivity = SingleLiveDataEmpty()
    val updateDatesAction = SingleLiveDataEmpty()
    val showHabitItemEditingFinished = SingleLiveDataEmpty()

    private val disposables = CompositeDisposable()


    init {
        setUpCalendar(0)
        disposables.add(
            habitListSharedUseCase
                .habitsMapSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ habitMap ->
                    habitListState.value = habitMap[selectedDate]
                    //updateHabitList()
                }
        )
        //habitListState.value = habitListSharedUseCase.getHabitList(selectedDate)
    }

    /*fun onViewShown() {
        habitListState.value = habitListSharedUseCase.getHabitList(selectedDate)
    }*/


    override fun onCleared() {
        disposables.dispose()
    }

    fun onSwipeHabit(position: Int) {
        habitListState.value?.getOrNull(position)?.let { habit ->
            mainUseCase.deleteHabitItemState(selectedDate, habit)
        }
    }

    fun onChangeEnableState(habit: HabitEntity) {
        mainUseCase.changeEnableHabitState(selectedDate, habit)
    }

    fun onPrevMonthButtonClick() {
        setUpCalendar(-1)
    }

    fun onNextMonthButtonClick() {
        setUpCalendar(1)
    }

    fun onDayClick(position: Int) {
        dates.find { it.isSelected }?.let { it.isSelected = false }
        dates.getOrNull(position)?.let { date ->
            selectedDate = selectedDate.copy(day = date.dayOfTheMonth)
            date.isSelected = true
            updateDatesAction.call()
            updateHabitList()
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
        showHabitItemEditingFinished.call()
    }

    private fun setUpCalendar(deltaMonth: Int) {
        val monthSdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        val monthCalendar = Calendar.getInstance()
        monthCalendar.set(Calendar.DAY_OF_MONTH, selectedDate.day)
        monthCalendar.set(Calendar.MONTH, selectedDate.month)
        monthCalendar.set(Calendar.YEAR, selectedDate.year)
        monthCalendar.add(Calendar.MONTH, deltaMonth)
        val maxDaysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

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

        val selectedDayPosition = selectedDate.day - 1
        scrollDayPosition = when {
            selectedDayPosition > 2 -> selectedDayPosition - 3
            maxDaysInMonth - selectedDayPosition < 2 -> selectedDayPosition
            else -> selectedDayPosition
        }

        monthTitleState.value = monthSdf.format(monthCalendar.time)
        updateDatesAction.call()
        scrollDatesToPositionAction.value = scrollDayPosition
        updateHabitList()
    }

    private fun updateHabitList() {
        habitListState.value = habitListSharedUseCase.getHabitsMap()[selectedDate]?: emptyList()
    }
}
