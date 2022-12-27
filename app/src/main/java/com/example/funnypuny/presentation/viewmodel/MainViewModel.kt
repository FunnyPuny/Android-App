package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.data.HabitRepositoryImpl
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.interactors.MainInteractor
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.common.SingleLiveDataEmpty

class MainViewModel(
    private val mainUseCase: MainUseCase
) : ViewModel() {

    val habitListState = MutableLiveData<List<HabitEntity>>() //состояние вью, всегда мутабельная лайв дата
    //val showConfirmDeleteDialog = SingleLiveDataEmpty() //какое-то действие, которое нужно сделать один раз

    init {
        habitListState.value = mainUseCase.getHabitList()
    }

    //----------------


    fun deleteHabitItem(habit: HabitEntity) {
        //mainUseCase.deleteHabitItem(habit)
    }

    fun changeEnableState(habit: HabitEntity) {
        val newItem = habit.copy(enabled = !habit.enabled)
        //mainUseCase.editHabitItem(newItem)
    }

}