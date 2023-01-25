package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.StatisticUseCase

class StatisticViewModel(
    private val statisticUseCase: StatisticUseCase
): ViewModel() {

    val habitListState = MutableLiveData<HabitEntity>()

    var action: HabitAction? = null
    var id: Int? = null
    var isAlreadyInited = false
    var inputName: String? = null

    fun init(action: HabitAction, id: Int) {
        if (isAlreadyInited) return
        this.action = action
        this.id = id

        when(action){
            HabitAction.ADD -> Unit
            HabitAction.EDIT -> initHabitItem(id)
        }

        isAlreadyInited = true

    }

    private fun initHabitItem(habitItemId: Int) {
        /* val item = getHabitItemUseCase.getHabitItem(habitItemId)
         _habit.value = item*/
        habitListState.value = statisticUseCase.getHabitItem(habitItemId)
    }


}
enum class HabitAction() {
    ADD,
    EDIT
}