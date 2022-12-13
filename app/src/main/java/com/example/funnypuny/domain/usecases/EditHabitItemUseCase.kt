package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.repository.HabitListRepository
import com.example.funnypuny.domain.entity.Habit

class EditHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    fun editHabitItem(habit: Habit){
        habitListRepository.editHabitItem(habit)
    }

}