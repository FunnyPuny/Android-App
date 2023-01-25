package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.GetHabitListUseCase
import com.example.funnypuny.domain.usecases.StatisticUseCase

class StatisticInteractor(
    private val habitRepository: HabitRepository
) : StatisticUseCase {

    override fun getHabitList(): List<HabitEntity> {
        return habitRepository.getHabitList()
    }

    override fun getHabitItem(habitItemId: Int): HabitEntity {
        return habitRepository.getHabitItem(habitItemId)
    }

}