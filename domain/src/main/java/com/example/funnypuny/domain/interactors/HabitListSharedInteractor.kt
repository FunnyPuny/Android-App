package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.HabitListSharedUseCase

class HabitListSharedInteractor(private val habitRepository: HabitRepository):HabitListSharedUseCase {
    override fun getHabitList(): List<HabitEntity> = habitRepository.getHabitList()
}