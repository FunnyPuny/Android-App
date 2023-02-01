package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.HabitEntity
import java.util.Date

interface StatisticUseCase {

    fun initState(): StatisticInitState


}

data class StatisticInitState(
    val completedHabitCount: Int,
    val missedHabitCount: Int,
    val percentHabit: Int,
    val currentDate: Date
)