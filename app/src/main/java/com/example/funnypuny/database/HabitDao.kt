package com.example.funnypuny.database

import androidx.room.*
import com.example.funnypuny.domain.entity.Habit

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    open fun getAll(): MutableList<Habit?>?

    @Query("SELECT * FROM habit WHERE id = :id")
    fun getById(id: Long): Habit?

    @Insert
    fun insert(habit: Habit?)

    @Update
    fun update(habit: Habit?)

    @Delete
    fun delete(habit: Habit?)
}