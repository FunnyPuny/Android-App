package com.example.funnypuny.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.example.funnypuny.domain.entity.Habit

@Database(entities = [ Habit::class ], version = 1)
abstract class HabitDataBase: RoomDatabase() {

    abstract fun habitDao(): HabitDao

}