package com.example.funnypuny.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.funnypuny.data.database.AppDatabase
import com.example.funnypuny.data.database.HabitDao
import org.koin.dsl.module

val daoModule = module {
    single { createDataBase(get()) }
    single { createHabitDao(get()) }
}

private fun createDataBase(app: Application): AppDatabase =
    Room
        .databaseBuilder(app, AppDatabase::class.java, "AppDatabase")
        .build()

private fun createHabitDao(database: AppDatabase): HabitDao =
    database.habitDao()

