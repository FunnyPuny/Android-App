package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.repository.HabitListRepository
import com.example.funnypuny.domain.entity.Habit

class GetHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    fun getHabitItem(habitItemId: Int): Habit {
        return habitListRepository.getHabitItem(habitItemId)
    }

}