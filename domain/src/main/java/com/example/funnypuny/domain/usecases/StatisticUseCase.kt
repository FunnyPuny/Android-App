package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.HabitEntity

interface StatisticUseCase {

    fun getHabitList(): List<HabitEntity>

    fun getHabitItem(habitItemId: Int): HabitEntity

}