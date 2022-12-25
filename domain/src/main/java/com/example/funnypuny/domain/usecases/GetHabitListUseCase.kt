package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.entity.HabitEntity

class GetHabitListUseCase(private val habitListRepository: HabitRepository) {

    fun getHabitList(): List<HabitEntity>{
        return habitListRepository.getHabitList()
    }
}