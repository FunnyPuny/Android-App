package com.example.funnypuny.presentation.di

import com.example.funnypuny.domain.interactors.MainInteractor
import com.example.funnypuny.domain.interactors.StatisticInteractor
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.domain.usecases.StatisticUseCase
import org.koin.dsl.module

val useCasesModule = module {
    factory<MainUseCase> { MainInteractor(get(),get()) }
    factory<StatisticUseCase> { StatisticInteractor(get()) }
}