package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.DayOfWeek
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.HabitListSharedUseCase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject

class HabitListSharedInteractor(private val habitRepository: HabitRepository):HabitListSharedUseCase {
    override fun habitsMapSubject(): Observable<Map<DateEntity,List<HabitEntity>>> =
        habitRepository.updateHabitsSubject().map { habitRepository.getHabitMap() }

    override fun getHabitsMap(): Map<DateEntity, List<HabitEntity>> = habitRepository.getHabitMap()

}