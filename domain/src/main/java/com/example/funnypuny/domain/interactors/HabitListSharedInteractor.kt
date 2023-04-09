package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.DayOfWeek
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.HabitListSharedUseCase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.Subject

class HabitListSharedInteractor(private val habitRepository: HabitRepository) :
    HabitListSharedUseCase {
    override fun habitsSubject(date: DateEntity): Observable<List<HabitEntity>> =
        habitRepository
            .updateHabitsSubject()
            .observeOn(Schedulers.io())
            .flatMap {
                habitRepository
                    .getHabitMap()
                    .map { it[date] ?: emptyList() }
                    .toObservable()
            }
}