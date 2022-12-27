package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.HabitEntity

interface MainUseCase {
    fun getHabitList(): List<HabitEntity>

    fun addHabitItem(habit: HabitEntity)

    fun editHabitItem(habit: HabitEntity)

    fun deleteHabitItem(habit: HabitEntity)

    fun getHabitItem(habitItemId: Int): HabitEntity
}