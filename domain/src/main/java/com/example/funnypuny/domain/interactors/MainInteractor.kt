package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.MainUseCase

class MainInteractor(private val habitRepository: HabitRepository) : MainUseCase {

    override fun getHabitList(): List<HabitEntity> {

        return habitRepository.getHabitList()
    }
}