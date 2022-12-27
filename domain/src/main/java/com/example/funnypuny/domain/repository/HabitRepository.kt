package com.example.funnypuny.domain.repository

import com.example.funnypuny.domain.entity.HabitEntity

interface HabitRepository {

    //fun habitsSubject():Subject<List<HabitEntity>>

    fun getHabitList(): List<HabitEntity>

    fun addHabitItem(habit: HabitEntity)

    fun editHabitItem(habit: HabitEntity)

    fun deleteHabitItem(habit: HabitEntity)

    fun getHabitItem(habitItemId: Int): HabitEntity


}