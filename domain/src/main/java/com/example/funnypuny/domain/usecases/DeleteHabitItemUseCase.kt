package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.entity.HabitEntity

class DeleteHabitItemUseCase(private val habitListRepository: HabitRepository) {

    fun deleteHabitItem(habit: HabitEntity){
        habitListRepository.deleteHabitItem(habit)
    }

}