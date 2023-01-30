package com.example.funnypuny.presentation.di

import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.presentation.viewmodel.HabitItemViewModel
import com.example.funnypuny.presentation.viewmodel.MainViewModel
import com.example.funnypuny.presentation.viewmodel.StatisticViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { (action: HabitActionEntity) -> HabitItemViewModel(action, get()) }
    viewModel { StatisticViewModel(get(),get()) }
}