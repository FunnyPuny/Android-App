package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitActionEntity

interface MainUseCase {

    fun changeEnableHabitState(date: DateEntity, habit: HabitEntity): List<HabitEntity>

    fun deleteHabitItemState(date: DateEntity, habit: HabitEntity): List<HabitEntity>

    fun getHabitItem(date: DateEntity, habitItemId: Int): HabitEntity?

    fun actionHabitState(action: HabitActionEntity, inputName: String?): MainActionHabitState

    //fun habitsState(): Observable<List<HabitEntity>>

}

sealed class MainActionHabitState{
    object Success: MainActionHabitState()
    object EmptyNameError: MainActionHabitState()
    object HabitNotFoundError: MainActionHabitState()

    data class Error(val error: Throwable) : MainActionHabitState()
}