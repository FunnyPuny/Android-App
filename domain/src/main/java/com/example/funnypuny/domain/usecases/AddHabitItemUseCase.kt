package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.repository.HabitListRepository
import com.example.funnypuny.domain.entity.Habit

class AddHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    fun addHabitItem(habit: Habit){
        habitListRepository.addHabitItem(habit)
    }

}