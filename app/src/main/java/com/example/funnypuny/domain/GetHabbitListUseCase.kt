package com.example.funnypuny.domain

import androidx.lifecycle.LiveData

class GetHabbitListUseCase(private val habbitListRepository: HabbitListRepository) {

    fun getHabbbitList(): LiveData<List<HabbitItem>>{
        return habbitListRepository.getHabbitList()
    }

}