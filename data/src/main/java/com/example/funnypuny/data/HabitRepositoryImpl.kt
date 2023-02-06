package com.example.funnypuny.data

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.DayOfWeek
import com.example.funnypuny.domain.repository.HabitRepository

class HabitRepositoryImpl : HabitRepository {

    private var autoIncrementId = 0

    private val habitMap = mutableMapOf<DateEntity,List<HabitEntity>>()

    init {

        /*for (i in 0 until 3) {
            addHabitItem(HabitEntity("Name: $i", true, i), null)
            autoIncrementId = i
        }*/
    }

    override fun getHabitList(date: DateEntity): List<HabitEntity> {
        return habitMap[date]?: emptyList()
    }


    override fun addHabitItem(date: DateEntity, habit: HabitEntity, indexPosition: Int?) {
        if (habit.id == HabitEntity.UNDEFINED_ID) {
            autoIncrementId++
            habit.id = autoIncrementId
        }
        val habitList = getHabitList(date).toMutableList()
        indexPosition
            ?.let { habitList.add(it, habit) }
            ?: habitList.add(habit)

        habitMap[date] = habitList
    }

    override fun deleteHabitItem(date: DateEntity, habit: HabitEntity) {
        val habitList = getHabitList(date).toMutableList()
        habitList.remove(habit)
        habitMap[date] = habitList
    }

    override fun getHabitItem(date: DateEntity, habitItemId: Int): HabitEntity? =

        habitMap[date]?.find { it.id == habitItemId }

}
