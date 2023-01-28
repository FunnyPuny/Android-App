package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.repository.HabitRepository

class SharedGetHabitListUseCase(private val habitRepository: HabitRepository) {

    fun getHabitList(): List<HabitEntity> {
        return habitRepository.getHabitList()
    }

}