package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import io.reactivex.rxjava3.core.Observable

interface MainUseCase {

    fun changeEnableHabitState(date: DateEntity, habit: HabitEntity): Observable<MainChangeHabitState>

    fun deleteHabitItemState(date: DateEntity, habit: HabitEntity): Observable<MainChangeHabitState>

    fun getHabitItem(date: DateEntity, habitItemId: Int):  Observable<MainGetHabitItemState>

    fun actionHabitState(action: HabitActionEntity, inputName: String?): Observable<MainActionHabitState>

    //fun habitsState(): Observable<List<HabitEntity>>

}

sealed class MainActionHabitState{
    object Start: MainActionHabitState()
    object Success: MainActionHabitState()
    object EmptyNameError: MainActionHabitState()
    object HabitNotFoundError: MainActionHabitState()

    data class Error(val error: Throwable) : MainActionHabitState()
}

sealed class MainChangeHabitState {
    object Start: MainChangeHabitState()
    object Success: MainChangeHabitState()

    data class Error(val error: Throwable) : MainChangeHabitState()
}


sealed class MainGetHabitItemState {
    object Start: MainGetHabitItemState()
    data class Success(val habit: HabitEntity): MainGetHabitItemState()
    object HabitNotFoundError: MainGetHabitItemState()

    data class Error(val error: Throwable) : MainGetHabitItemState()
}