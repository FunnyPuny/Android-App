package com.example.funnypuny.domain

class GetHabbitListUseCase(private val habbitListRepository: HabbitListRepository) {

    fun getHabbbitList(): List<HabbitItem>{
        return habbitListRepository.getHabbitList()
    }

}