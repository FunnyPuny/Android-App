package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.entity.HabitEntity

class GetHabitItemUseCase(private val habitListRepository: HabitRepository) {

    fun getHabitItem(habitItemId: Int): HabitEntity {
        return habitListRepository.getHabitItem(habitItemId)
    }

}