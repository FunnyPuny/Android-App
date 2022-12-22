package com.example.funnypuny.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.example.funnypuny.domain.entity.Habit

@Database(entities = [ Habit::class ], version = 1)
abstract class HabitDataBase: RoomDatabase() {

    companion object {

        private var db: HabitDataBase? = null
        private const val DB_NAME = "habit-database"
        private val LOCK = Any()

        fun getInstance(context: Context): HabitDataBase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context,
                        HabitDataBase::class.java,
                        DB_NAME
                    ).build()
                db = instance
                return instance
            }
        }
    }

    abstract fun habitDao(): HabitDao

}