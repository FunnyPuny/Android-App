package com.example.funnypuny.domain

class DeleteHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    fun deleteHabitItem(habitItem: HabitItem){
        habitListRepository.deleteHabitItem(habitItem)
    }

}