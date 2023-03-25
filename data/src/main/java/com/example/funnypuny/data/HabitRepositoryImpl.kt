package com.example.funnypuny.data

import com.example.funnypuny.data.database.*
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

    private val habitsEntityMapper by lazy { HabitsEntityMapper() }
    private val habitMapper by lazy { HabitMapper() }


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
                        val habitsEntity = habitsEntityMapper.apply(habits)
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

        return habitDao.insertAll(habitMapper.apply(date to habit))
    }

    override fun deleteHabitItem(habitId: Int): Completable =
        habitDao.delete(habitId)

    override fun getHabitItem(date: DateEntity, habitItemId: Int): HabitEntity? =
        habitMap[date]?.find { it.id == habitItemId }

    override fun editHabit(date: DateEntity, habit: HabitEntity): Completable =
        habitDao.edit(habitMapper.apply(Pair(date,habit)))


    /*private fun getHabitList(date: DateEntity): List<HabitEntity> {
        return habitMap[date] ?: emptyList()
    }*/

}
