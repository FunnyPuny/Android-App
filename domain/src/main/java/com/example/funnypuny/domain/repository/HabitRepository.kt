package com.example.funnypuny.domain.repository

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitFrequencyEntity

interface HabitRepository {

    //fun habitsSubject():Subject<List<HabitEntity>>

    fun getHabitList(): List<HabitEntity>

    fun addHabitItem(habit: HabitEntity)

    fun editHabitItem(habit: HabitEntity): List<HabitEntity>

    fun deleteHabitItem(habit: HabitEntity): List<HabitEntity>

    fun getHabitItem(habitItemId: Int): HabitEntity


}