package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.MainUseCase

class MainViewModel(
    private val mainUseCase: MainUseCase
) : ViewModel() {

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

}