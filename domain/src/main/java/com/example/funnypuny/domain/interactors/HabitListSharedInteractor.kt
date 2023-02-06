package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.DayOfWeek
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.HabitListSharedUseCase

class HabitListSharedInteractor(private val habitRepository: HabitRepository):HabitListSharedUseCase {
    override fun getHabitList(date: DateEntity): List<HabitEntity> = habitRepository.getHabitList(date)
}