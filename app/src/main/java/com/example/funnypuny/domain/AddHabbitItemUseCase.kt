package com.example.funnypuny.domain

class AddHabbitItemUseCase(private val habbitListRepository: HabbitListRepository) {

    fun addHabbitItem(habbitItem: HabbitItem){
        habbitListRepository.addHabbitItem(habbitItem)
    }

}