package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.repository.HabitListRepository
import com.example.funnypuny.domain.entity.Habit

class DeleteHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    fun deleteHabitItem(habit: Habit){
        habitListRepository.deleteHabitItem(habit)
    }

}