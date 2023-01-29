package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.HabitEntity

interface HabitListSharedUseCase {

    fun getHabitList(): List<HabitEntity>

}