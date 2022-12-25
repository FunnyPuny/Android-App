package com.example.funnypuny.domain.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.funnypuny.data.database.HabitDataBase
import com.example.funnypuny.domain.entity.Habit

private const val DATABASE_NAME = "habit-database"

class HabitRepository private constructor(context: Context){

    private val database: HabitDataBase = Room.databaseBuilder(
        context.applicationContext,
        HabitDataBase::class.java,
        DATABASE_NAME
    ).build()

    private val habitDao = database.habitDao()

    fun getAll(): LiveData<Habit?>? = habitDao.getAll()

    fun getById(id: Long): Habit? = habitDao.getById(id)

    fun insert(habit: Habit?) = habitDao.insert(habit)

    fun update(habit: Habit?) = habitDao.update(habit)

    fun delete(habit: Habit?) = habitDao.delete(habit)

    companion object{
        private var INSTANCE: HabitRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null){
                INSTANCE = HabitRepository(context)
            }
        }

        fun get(): HabitRepository {
            return INSTANCE ?:
            throw IllegalStateException("HabitRepository must be initialized")
        }
    }
}
