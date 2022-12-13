package com.example.funnypuny.domain.usecases

import androidx.lifecycle.LiveData
import com.example.funnypuny.domain.repository.HabitListRepository
import com.example.funnypuny.domain.entity.FrequencyItem

class GetDayListUseCase(private val habitListRepository: HabitListRepository) {

    fun getDayList(): LiveData<List<FrequencyItem>> {
        return habitListRepository.getDayList()
    }

}