package com.example.funnypuny.domain.repository

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.DayOfWeek

interface HabitRepository {

    //fun habitsSubject():Subject<List<HabitEntity>>

    fun getHabitList(date: DateEntity): List<HabitEntity>

    fun addHabitItem(date: DateEntity, habit: HabitEntity, indexPosition: Int?)

    fun deleteHabitItem(date: DateEntity, habit: HabitEntity)

    fun getHabitItem(date: DateEntity, habitItemId: Int): HabitEntity?

}