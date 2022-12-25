package com.example.funnypuny.presentation.di

import com.example.funnypuny.data.HabitRepositoryImpl
import com.example.funnypuny.domain.repository.HabitRepository
import org.koin.dsl.module

val repositoryModule = module {
    single <HabitRepository> { HabitRepositoryImpl() }
}