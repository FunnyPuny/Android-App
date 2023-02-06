package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.MainActionHabitState
import com.example.funnypuny.domain.usecases.MainUseCase

class MainInteractor(
    private val habitRepository: HabitRepository
) : MainUseCase {

    override fun changeEnableHabitState(date: DateEntity, habit: HabitEntity): List<HabitEntity> {
        val newItem = habit.copy(enabled = !habit.enabled)
        habitRepository.getHabitList(date).let {
            habitRepository.deleteHabitItem(date, habit)
            habitRepository.addHabitItem(date, newItem, null)
        }
        return habitRepository.getHabitList(date)
    }

    override fun deleteHabitItemState(date: DateEntity, habit: HabitEntity): List<HabitEntity> {
        habitRepository.deleteHabitItem(date, habit)
        // habitRepository.habitsSubject().call()
        return habitRepository.getHabitList(date)
    }

    override fun getHabitItem(date: DateEntity, habitItemId: Int): HabitEntity? {
        return habitRepository.getHabitItem(date, habitItemId)
    }

    override fun actionHabitState(
        action: HabitActionEntity,
        inputName: String?
    ): MainActionHabitState =
        when (action) {
            is HabitActionEntity.Add -> addHabitState(action, inputName)
            is HabitActionEntity.Edit -> editHabitState(action, inputName)
        }


    private fun isHabitNameValid(name: String?): Boolean = !name.isNullOrBlank()

    private fun addHabitState(
        action: HabitActionEntity.Add,
        inputName: String?
    ): MainActionHabitState {
        if (isHabitNameValid(inputName)) {
            inputName?.let { name ->
                val habit = HabitEntity(name, true)
                habitRepository.addHabitItem(action.date, habit, null)
                return MainActionHabitState.Success
            }
        }
        return MainActionHabitState.EmptyNameError
    }

    private fun editHabitState(
        action: HabitActionEntity.Edit,
        inputName: String?
    ): MainActionHabitState {
        if (isHabitNameValid(inputName)) {
            inputName?.let { name ->
                habitRepository.getHabitItem(action.date, action.id)
                    ?.let { habit ->
                        val indexPosition = habitRepository.getHabitList(action.date).indexOf(habit)
                            .takeIf { it != -1 }
                        habitRepository.deleteHabitItem(action.date, habit)
                        habitRepository.addHabitItem(
                            date = action.date,
                            habit = habit.copy(name = name),
                            indexPosition = indexPosition
                        )
                        return MainActionHabitState.Success
                    }
                    ?: return MainActionHabitState.HabitNotFoundError
            }
        }
        return MainActionHabitState.EmptyNameError
    }

}