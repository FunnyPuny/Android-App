package com.example.funnypuny.domain.usecases

import androidx.lifecycle.LiveData
import com.example.funnypuny.domain.repository.HabitListRepository
import com.example.funnypuny.domain.entity.Frequency

class GetDayListUseCase(private val habitListRepository: HabitListRepository) {

    fun getDayList(): LiveData<List<Frequency>> {
        return habitListRepository.getDayList()
    }

}