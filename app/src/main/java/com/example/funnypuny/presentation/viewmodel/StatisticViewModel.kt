package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.HabitListSharedUseCase
import com.example.funnypuny.domain.usecases.StatisticUseCase
import com.example.funnypuny.presentation.common.SingleLiveData

class StatisticViewModel(
    private val statisticUseCase: StatisticUseCase,
    private val habitListSharedUseCase: HabitListSharedUseCase
): ViewModel() {

    val habitListState = MutableLiveData<List<HabitEntity>>()

    init {
        //habitListState.value = habitListSharedUseCase.getHabitList()
    }



}