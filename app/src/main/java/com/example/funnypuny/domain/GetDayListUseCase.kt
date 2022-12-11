package com.example.funnypuny.domain

import androidx.lifecycle.LiveData

class GetDayListUseCase(private val habitListRepository: HabitListRepository) {

    fun getDayList(): LiveData<List<FrequencyItem>> {
        return habitListRepository.getDayList()
    }

}