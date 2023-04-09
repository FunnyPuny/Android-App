package com.example.funnypuny.domain.repository

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.DayOfWeek
import com.example.funnypuny.domain.entity.Optional
import com.example.funnypuny.domain.usecases.MainGetHabitItemState
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.Subject

interface HabitRepository {

    fun updateHabitsSubject(): Subject<Unit>

    fun getHabitMap(): Single<Map<DateEntity, List<HabitEntity>>>

    //fun getHabitList(date: DateEntity): List<HabitEntity>

    fun addHabitItem(date: DateEntity, habit: HabitEntity, indexPosition: Int?): Completable

    fun deleteHabitItem(habitId: Int): Completable

    fun getHabitItem(habitItemId: Int): Single<HabitGetHabitItemState>

    fun editHabit(date: DateEntity, habit: HabitEntity): Completable

}

sealed class HabitGetHabitItemState() {
    data class Success(val habit: HabitEntity): HabitGetHabitItemState()
    object HabitNotFoundError: HabitGetHabitItemState()

    data class Error(val error: Throwable) : HabitGetHabitItemState()
}