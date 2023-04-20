package com.example.funnypuny.domain.repository

import com.example.funnypuny.domain.entity.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.Subject

interface HabitRepository {

    fun updateHabitsSubject(): Subject<Unit>

    fun getHabitMap(): Single<Map<DateEntity, List<HabitEntity>>>

    fun addHabitItem(date: DateEntity, week: WeekEntity, habit: HabitEntity): Completable

    fun deleteHabitItem(habitId: Int): Completable

    fun getHabitItem(habitItemId: Int): Single<HabitGetHabitItemState>

    fun editHabit(date: DateEntity, week: WeekEntity, habit: HabitEntity): Completable

    //habit по конкретной какой-то дате
    //fun getHabitList(date: DateEntity): Single<List<HabitEntity>>

    //привычки каждый день
    fun getEverydayHabitList(): Single<List<HabitEntity>>

    //привычки по дням недели
    fun getDayOfWeekHabitList(week: WeekEntity): Single<List<HabitEntity>>

}

sealed class HabitGetHabitItemState() {
    data class Success(val habit: HabitEntity): HabitGetHabitItemState()
    object HabitNotFoundError: HabitGetHabitItemState()

    data class Error(val error: Throwable) : HabitGetHabitItemState()
}