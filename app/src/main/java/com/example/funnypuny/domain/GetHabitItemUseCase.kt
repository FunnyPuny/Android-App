package com.example.funnypuny.domain

class GetHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    fun getHabitItem(habbitItemId: Int): HabitItem {
        return habitListRepository.getHabitItem(habbitItemId)
    }

}