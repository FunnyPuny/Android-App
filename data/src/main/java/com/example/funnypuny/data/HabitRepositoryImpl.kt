package com.example.funnypuny.data

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.DayOfWeek
import com.example.funnypuny.domain.repository.HabitRepository
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class HabitRepositoryImpl : HabitRepository {

    private var autoIncrementId = 0

    private val habitMap = mutableMapOf<DateEntity,List<HabitEntity>>()

    private val updateHabitsSubject = PublishSubject.create<Unit>()

    init {

        /*for (i in 0 until 3) {
            addHabitItem(HabitEntity("Name: $i", true, i), null)
            autoIncrementId = i
        }*/
    }

    override fun updateHabitsSubject(): Subject<Unit> = updateHabitsSubject
    override fun getHabitMap(): Map<DateEntity, List<HabitEntity>> = habitMap


    override fun addHabitItem(date: DateEntity, habit: HabitEntity, indexPosition: Int?) {
        if (habit.id == HabitEntity.UNDEFINED_ID) {
            autoIncrementId++
            habit.id = autoIncrementId
        }
        val habitList = getHabitList(date).toMutableList()
        //val habitList = getHabitMap()[date]?.toMutableList()?: mutableListOf()
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


    private fun getHabitList(date: DateEntity): List<HabitEntity> {
        return habitMap[date]?: emptyList()
    }

}
