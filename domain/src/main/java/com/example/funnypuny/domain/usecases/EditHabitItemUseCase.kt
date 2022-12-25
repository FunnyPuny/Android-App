package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.entity.HabitEntity

class EditHabitItemUseCase(private val habitListRepository: HabitRepository) {

    fun editHabitItem(habit: HabitEntity){
        habitListRepository.editHabitItem(habit)
    }

}