package com.example.funnypuny.domain.repository

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.DayOfWeek
import com.example.funnypuny.domain.entity.Optional
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.Subject

interface HabitRepository {

    fun updateHabitsSubject(): Subject<Unit>

    fun getHabitMap(): Single<Map<DateEntity, List<HabitEntity>>>

    //fun getHabitList(date: DateEntity): List<HabitEntity>

    fun addHabitItem(date: DateEntity, habit: HabitEntity, indexPosition: Int?): Completable

    fun deleteHabitItem(habitId: Int): Completable

    fun getHabitItem(habitItemId: Int): Single<HabitEntity>

    fun editHabit(date: DateEntity, habit: HabitEntity): Completable

}