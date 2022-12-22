package com.example.funnypuny.domain.repository

import androidx.lifecycle.LiveData
import com.example.funnypuny.domain.entity.Frequency
import com.example.funnypuny.domain.entity.Habit

interface HabitListRepository {

    fun addHabitItem(habit: Habit)

    fun editHabitItem(habit: Habit)

    fun deleteHabitItem(habit: Habit)

    fun getHabitItem(habitItemId: Int): Habit

    fun getHabitList(): LiveData<List<Habit>>

}