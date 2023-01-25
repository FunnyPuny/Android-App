package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.common.SingleLiveDataEmpty

class HabitItemViewModel(
    private val mainUseCase: MainUseCase
): ViewModel() {

    val errorInputNameState= MutableLiveData<Boolean>()

    val habitState= MutableLiveData<HabitEntity>()

    val shouldCloseScreenState = MutableLiveData<Unit>()

    private val data = ArrayList<HabitFrequencyEntity>()
    val daysOfTheWeekState = MutableLiveData<ArrayList<HabitFrequencyEntity>>()

    val showNewInstanceEditItem = SingleLiveDataEmpty()
    val showNewInstanceAddItem = SingleLiveDataEmpty()

    var action: HabitItemAction? = null
    var id: Int? = null
    var isAlreadyInited = false
    var inputName: String? = null


    init {
        data.add(HabitFrequencyEntity("Sun"))
        data.add(HabitFrequencyEntity("Mon"))
        data.add(HabitFrequencyEntity("Tue"))
        data.add(HabitFrequencyEntity("Wed"))
        data.add(HabitFrequencyEntity("Thu"))
        data.add(HabitFrequencyEntity("Fri"))
        data.add(HabitFrequencyEntity("Sat"))
        daysOfTheWeekState.value = data
    }

    fun init(action: HabitItemAction, id: Int) {
        if (isAlreadyInited) return
        this.action = action
        this.id = id

        when(action){
            HabitItemAction.ADD -> Unit
            HabitItemAction.EDIT -> initHabitItem(id)
        }

        isAlreadyInited = true

        when (action) {
            HabitItemAction.ADD -> showNewInstanceAddItem.call()
            HabitItemAction.EDIT -> showNewInstanceEditItem.call()
        }
    }

    fun onNameChanged(inputName: String?) {
        this.inputName = inputName
    }

    //todo нужно убрать inputName на вход и сетить его в отдельную переменную по аналогии с методом init
    fun onSaveClick() {
        when (action) {
            HabitItemAction.ADD -> addHabitItem(inputName)
            HabitItemAction.EDIT -> editHabitItem(inputName)
            null -> Unit
        }
    }


    private fun initHabitItem(habitItemId: Int) {
        habitState.value = mainUseCase.getHabitItem(habitItemId)
    }

    private fun addHabitItem(inputName: String?) {
        val fieldsValid = validateInput(inputName)
        if (fieldsValid) {
            inputName?.let { name ->
                val habit = HabitEntity(name,true)
                mainUseCase.addHabitItem(habit)
                finishWork()
            }
        }
    }

    private fun editHabitItem(inputName: String?) {
        val fieldsValid = validateInput(inputName)
        if (fieldsValid) {
            inputName?.let { name ->
                habitState.value?.let { habit ->
                    val item = habit.copy(name = name)
                    mainUseCase.editHabitItem(item)
                    finishWork()
                }
            }
        }
    }

    private fun validateInput(name: String?): Boolean {
        var result = true
        if ( name.isNullOrBlank() ) {
            errorInputNameState.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        errorInputNameState.value = false
    }


    private fun finishWork() {
        shouldCloseScreenState.value = Unit
    }

}

enum class HabitItemAction() {
    ADD,
    EDIT
}