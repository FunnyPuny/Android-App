package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.HabitEntity

interface MainUseCase {
    fun getHabitList(): List<HabitEntity>
}