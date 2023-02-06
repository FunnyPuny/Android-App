package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.DayOfWeek

interface HabitListSharedUseCase {

    fun getHabitList(date: DateEntity): List<HabitEntity>

}