package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.repository.HabitListRepository
import com.example.funnypuny.domain.entity.Habit

class GetHabitListUseCase(private val habitListRepository: HabitListRepository) {

    fun getHabitList(): MutableList<List<Habit>>{
        return habitListRepository.getHabitList()
    }

}