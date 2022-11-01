package com.example.funnypuny.domain

class AddHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    fun addHabitItem(habitItem: HabitItem){
        habitListRepository.addHabitItem(habitItem)
    }

}