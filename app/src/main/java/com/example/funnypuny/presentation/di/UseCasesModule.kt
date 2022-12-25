package com.example.funnypuny.presentation.di

import com.example.funnypuny.domain.interactors.MainInteractor
import com.example.funnypuny.domain.usecases.MainUseCase
import org.koin.dsl.module

val useCasesModule = module {
    factory<MainUseCase> { MainInteractor(get()) }
}