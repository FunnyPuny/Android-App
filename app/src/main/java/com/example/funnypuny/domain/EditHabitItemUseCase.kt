package com.example.funnypuny.domain

class EditHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    fun editHabitItem(habitItem: HabitItem){
        habitListRepository.editHabitItem(habitItem)
    }

}