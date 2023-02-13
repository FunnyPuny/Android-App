package com.example.funnypuny.presentation.di

import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.presentation.viewmodel.HabitItemActivityViewModel
import com.example.funnypuny.presentation.viewmodel.HabitItemFragmentViewModel
import com.example.funnypuny.presentation.viewmodel.MainViewModel
import com.example.funnypuny.presentation.viewmodel.StatisticViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { (action: HabitActionEntity) -> HabitItemFragmentViewModel(action, get()) }
    viewModel { (action: HabitActionEntity) -> HabitItemActivityViewModel(action) }
    viewModel { StatisticViewModel(get(),get()) }
}