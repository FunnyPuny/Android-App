package com.example.funnypuny.domain.usecases

import androidx.lifecycle.LiveData
import com.example.funnypuny.domain.repository.HabitListRepository
import com.example.funnypuny.domain.entity.Habit

class GetHabitListUseCase(private val habitListRepository: HabitListRepository) {

    fun getHabitList(): LiveData<List<Habit>>{
        return habitListRepository.getHabitList()
    }

}