package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.entity.HabitEntity

class AddHabitItemUseCase(private val habitRepository: HabitRepository) {

    fun addHabitItem(habit: HabitEntity){
        habitRepository.addHabitItem(habit)
    }

}