package com.example.funnypuny.domain

class GetHabbitItemUseCase(private val habbitListRepository: HabbitListRepository) {

    fun getHabbitItem(habbitItemId: Int): HabbitItem {
        return habbitListRepository.getHabbitItem(habbitItemId)
    }

}