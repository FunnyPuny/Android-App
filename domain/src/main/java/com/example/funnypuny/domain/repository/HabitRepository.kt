package com.example.funnypuny.domain.repository

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitFrequencyEntity

interface HabitRepository {

    //fun habitsSubject():Subject<List<HabitEntity>>

    fun getHabitList(): List<HabitEntity>

    fun addHabitItem(habit: HabitEntity, indexPosition: Int?)

    fun deleteHabitItem(habit: HabitEntity)

    fun getHabitItem(habitItemId: Int): HabitEntity?

}