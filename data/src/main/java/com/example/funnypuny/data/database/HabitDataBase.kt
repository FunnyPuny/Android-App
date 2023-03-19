
package com.example.funnypuny.data.database


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}
