package com.example.funnypuny.domain

class EditHabbitItemUseCase(private val habbitListRepository: HabbitListRepository) {

    fun editHabbitItem(habbitItem: HabbitItem){
        habbitListRepository.editHabbitItem(habbitItem)
    }

}