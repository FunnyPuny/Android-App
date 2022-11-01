package com.example.funnypuny.domain

import androidx.lifecycle.LiveData

class GetHabitListUseCase(private val habitListRepository: HabitListRepository) {

    fun getHabitList(): LiveData<List<HabitItem>>{
        return habitListRepository.getHabitList()
    }

}