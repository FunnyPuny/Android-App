package com.example.funnypuny.domain

class GetHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    fun getHabitItem(habitItemId: Int): HabitItem {
        return habitListRepository.getHabitItem(habitItemId)
    }

}