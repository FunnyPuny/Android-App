package com.example.funnypuny.domain

class DeleteHabbitItemUseCase(private val habbitListRepository: HabbitListRepository) {

    fun deleteHabbitItem(habbitItem: HabbitItem){
        habbitListRepository.deleteHabbitItem(habbitItem)
    }

}