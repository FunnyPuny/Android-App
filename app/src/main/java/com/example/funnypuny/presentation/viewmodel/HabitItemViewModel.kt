package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.usecases.MainActionHabitState
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.common.SingleLiveData
import com.example.funnypuny.presentation.common.SingleLiveDataEmpty

class HabitItemViewModel(
    private val action: HabitActionEntity,
    private val mainUseCase: MainUseCase
): ViewModel() {

    val errorInputNameState= MutableLiveData<Boolean>()

    val habitState= MutableLiveData<HabitEntity>()

    val shouldCloseScreenState = MutableLiveData<Unit>()

    private val data = ArrayList<HabitFrequencyEntity>()
    val daysOfTheWeekState = MutableLiveData<ArrayList<HabitFrequencyEntity>>()

    val showNewInstanceEditItem = SingleLiveData<Int>()
    val showNewInstanceAddItem = SingleLiveDataEmpty()

    private var inputName: String? = null

    init {
        data.add(HabitFrequencyEntity("Sun"))
        data.add(HabitFrequencyEntity("Mon"))
        data.add(HabitFrequencyEntity("Tue"))
        data.add(HabitFrequencyEntity("Wed"))
        data.add(HabitFrequencyEntity("Thu"))
        data.add(HabitFrequencyEntity("Fri"))
        data.add(HabitFrequencyEntity("Sat"))
        daysOfTheWeekState.value = data

        when(action){
            is HabitActionEntity.Add -> Unit
            is HabitActionEntity.Edit -> onInitHabitItem(action.id)
        }

        //todo разобраться с типом livedata
        when (action) {
            is HabitActionEntity.Add -> showNewInstanceAddItem.call()
            is HabitActionEntity.Edit -> showNewInstanceEditItem.value = action.id
        }
    }

    fun onNameChanged(inputName: String?) {
        this.inputName = inputName
        errorInputNameState.value = false
    }

    fun onSaveClick() {
        when ( mainUseCase.actionHabitState(action, inputName) ) {
            is MainActionHabitState.Success -> onFinishWork()
            is MainActionHabitState.EmptyNameError -> errorInputNameState.value = true
            is MainActionHabitState.HabitNotFoundError -> shouldCloseScreenState.value = Unit
            is MainActionHabitState.Error -> Unit
        }
    }

    /*fun onResetErrorInputName() {
        errorInputNameState.value = false
    }*/

    private fun onInitHabitItem(habitItemId: Int) {
        mainUseCase.getHabitItem(habitItemId)
            ?.let { habit -> habitState.value = habit }
            ?: onFinishWork()
    }

    /*private fun onAddHabitItem(inputName: String?) {
        when ( mainUseCase.addHabitItemState(inputName) ) {
            is MainActionHabitState.Success -> onFinishWork()
            is MainActionHabitState.EmptyName -> errorInputNameState.value = true
            is MainActionHabitState.Error -> Unit
        }
    }*/

    /*private fun onEditHabitItem(inputName: String?) {
        val fieldsValid = validateInput(inputName)
        if (fieldsValid) {
            inputName?.let { name ->
                habitState.value?.let { habit ->
                    val item = habit.copy(name = name)
                    mainUseCase.editHabitItemState(item)
                    onFinishWork()
                }
            }
        }
    }*/

    /*private fun validateInput(name: String?): Boolean {
        var result = true
        if ( name.isNullOrBlank() ) {
            errorInputNameState.value = true
            result = false
        }
        return result
    }*/

    //todo убрать
    private fun onFinishWork() {
        shouldCloseScreenState.value = Unit
    }

}
