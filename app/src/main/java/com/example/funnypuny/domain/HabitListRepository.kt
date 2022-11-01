package com.example.funnypuny.domain

import androidx.lifecycle.LiveData

interface HabitListRepository {

    fun addHabitItem(habitItem: HabitItem)

    fun editHabitItem(habitItem: HabitItem)

    fun deleteHabitItem(habitItem: HabitItem)

    fun getHabitItem(habitItemId: Int): HabitItem

    fun getHabitList(): LiveData<List<HabitItem>>

}