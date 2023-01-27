package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.GetHabitListUseCase
import com.example.funnypuny.domain.usecases.MainUseCase

class MainInteractor(private val habitRepository: HabitRepository) : MainUseCase {

    override fun getHabitList(): List<HabitEntity> {
        return habitRepository.getHabitList()
    }

    override fun addHabitItem(habit: HabitEntity) {
        habitRepository.addHabitItem(habit)
    }

    override fun editHabitItem(habit: HabitEntity): List<HabitEntity> {
        habitRepository.editHabitItem(habit)
        return habitRepository.getHabitList()
    }

    override fun deleteHabitItem(habit: HabitEntity): List<HabitEntity> {
        habitRepository.deleteHabitItem(habit)
        // habitRepository.habitsSubject().call()
        return habitRepository.getHabitList()
    }

    override fun getHabitItem(habitItemId: Int): HabitEntity {
        return habitRepository.getHabitItem(habitItemId)
    }


}