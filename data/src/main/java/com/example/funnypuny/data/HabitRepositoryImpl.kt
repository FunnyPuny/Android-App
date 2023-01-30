package com.example.funnypuny.data

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.repository.HabitRepository

class HabitRepositoryImpl : HabitRepository {

    private val habitList = mutableListOf<HabitEntity>()

    private var autoIncrementId = 0

    init {
        for (i in 0 until 3) {
            addHabitItem(HabitEntity("Name: $i", true, i))
            autoIncrementId = i
        }
    }

    override fun getHabitList(): List<HabitEntity> {
        return habitList
    }

    override fun addHabitItem(habit: HabitEntity) {
        //при редоктировании элемента надо сохранить его id
        //если id данного элемента неопределен, то в этом случае мы его установим
        if (habit.id == HabitEntity.UNDEFINED_ID) {
            autoIncrementId++
            habit.id = autoIncrementId
        }
        habitList.add(habit)
    }

    override fun deleteHabitItem(habit: HabitEntity) {
        habitList.remove(habit)
    }

    override fun getHabitItem(habitItemId: Int): HabitEntity? =
        habitList.find { it.id == habitItemId }

}