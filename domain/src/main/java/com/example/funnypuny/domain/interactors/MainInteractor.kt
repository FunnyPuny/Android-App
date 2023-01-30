package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.MainActionHabitState
import com.example.funnypuny.domain.usecases.MainUseCase

class MainInteractor(
    private val habitRepository: HabitRepository
) : MainUseCase {

    override fun changeEnableHabitState(habit: HabitEntity): List<HabitEntity> {
        val newItem = habit.copy(enabled = !habit.enabled)
        habitRepository.getHabitList().let {
            habitRepository.deleteHabitItem(habit)
            habitRepository.addHabitItem(newItem)
        }
        return habitRepository.getHabitList()
    }

    override fun deleteHabitItemState(habit: HabitEntity): List<HabitEntity> {
        habitRepository.deleteHabitItem(habit)
        // habitRepository.habitsSubject().call()
        return habitRepository.getHabitList()
    }

    override fun getHabitItem(habitItemId: Int): HabitEntity? {
        return habitRepository.getHabitItem(habitItemId)
    }

    override fun actionHabitState(
        action: HabitActionEntity,
        inputName: String?
    ): MainActionHabitState =
        when (action) {
            is HabitActionEntity.Add -> addHabitState(inputName)
            is HabitActionEntity.Edit -> editHabitState(action.id, inputName)
        }


    private fun isHabitNameValid(name: String?): Boolean = !name.isNullOrBlank()

    private fun addHabitState(inputName: String?): MainActionHabitState {
        if (isHabitNameValid(inputName)) {
            inputName?.let { name ->
                val habit = HabitEntity(name, true)
                habitRepository.addHabitItem(habit)
                return MainActionHabitState.Success
            }
        }
        return MainActionHabitState.EmptyNameError
    }

    private fun editHabitState(id: Int, inputName: String?): MainActionHabitState {
        if (isHabitNameValid(inputName)) {
            inputName?.let { name ->
                habitRepository.getHabitItem(id)
                    ?.let { habit ->
                        habitRepository.deleteHabitItem(habit)
                        habitRepository.addHabitItem(habit.copy(name = name))
                        return MainActionHabitState.Success
                    }
                    ?: return MainActionHabitState.HabitNotFoundError
            }
        }
        return MainActionHabitState.EmptyNameError
    }

}