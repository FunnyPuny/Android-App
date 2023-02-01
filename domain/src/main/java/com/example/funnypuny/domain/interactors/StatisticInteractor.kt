package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.HabitListSharedUseCase
import com.example.funnypuny.domain.usecases.StatisticUseCase

class StatisticInteractor(
    private val habitListSharedUseCase: HabitListSharedUseCase
) : StatisticUseCase {



}