package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.SharedGetHabitListUseCase
import com.example.funnypuny.domain.usecases.StatisticUseCase

class StatisticInteractor(
    private val sharedGetHabitListUseCase: SharedGetHabitListUseCase
) : StatisticUseCase {

    override fun getHabitList(): List<HabitEntity> {
        return sharedGetHabitListUseCase.getHabitList()
    }

}