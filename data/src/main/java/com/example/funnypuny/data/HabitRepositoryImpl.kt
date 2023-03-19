package com.example.funnypuny.data

import com.example.funnypuny.data.database.Habit
import com.example.funnypuny.data.database.HabitDao
import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.repository.HabitRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject

class HabitRepositoryImpl(private val habitDao: HabitDao) : HabitRepository {

    private var autoIncrementId = 0

    private val habitMap = mutableMapOf<DateEntity, List<HabitEntity>>()

    private val updateHabitsSubject = BehaviorSubject.createDefault(Unit)

    init {

        /*for (i in 0 until 3) {
            addHabitItem(HabitEntity("Name: $i", true, i), null)
            autoIncrementId = i
        }*/
    }

    override fun updateHabitsSubject(): Subject<Unit> = updateHabitsSubject
    override fun getHabitMap(): Single<Map<DateEntity, List<HabitEntity>>> =
        habitDao.getAll()
            .map { habits ->
                habits
                    .groupBy { DateEntity(it.day, it.month, it.year) }
                    .map { (dateEntity, habits) ->
                        val habitsEntity = habits.map { HabitEntity(it.name, it.enabled, it.id) }
                        Pair(dateEntity, habitsEntity)
                    }
                    .toMap()
            }


    override fun addHabitItem(
        date: DateEntity,
        habit: HabitEntity,
        indexPosition: Int?
    ): Completable {
        /*if (habit.id == HabitEntity.UNDEFINED_ID) {
            autoIncrementId++
            habit.id = autoIncrementId
        }
        val habitList = getHabitList(date).toMutableList()
        //val habitList = getHabitMap()[date]?.toMutableList()?: mutableListOf()
        indexPosition
            ?.let { habitList.add(it, habit) }
            ?: habitList.add(habit)

        habitMap[date] = habitList*/

        val habitDB = Habit(habit.id, habit.name, habit.enabled, date.day, date.month, date.year)
        return habitDao.insertAll(habitDB)
    }

    override fun deleteHabitItem(date: DateEntity, habit: HabitEntity): Completable =
        habitDao.delete(habit.id)

    override fun getHabitItem(date: DateEntity, habitItemId: Int): HabitEntity? =
        habitMap[date]?.find { it.id == habitItemId }


    private fun getHabitList(date: DateEntity): List<HabitEntity> {
        return habitMap[date] ?: emptyList()
    }

}
