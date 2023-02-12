package com.example.funnypuny.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.usecases.MainActionHabitState
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.common.SingleLiveData

class HabitItemFragmentViewModel(
    private val action: HabitActionEntity,
    private val mainUseCase: MainUseCase
) : ViewModel() {

    val errorInputNameState = MutableLiveData<Boolean>()

    //val habitState = MutableLiveData<HabitEntity>()

    val habitNameState = MutableLiveData<String>()

    val shouldCloseScreenState = MutableLiveData<Unit>()

    private val data = ArrayList<HabitFrequencyEntity>()
    val daysOfTheWeekState = MutableLiveData<ArrayList<HabitFrequencyEntity>>()

    val showAction = SingleLiveData<HabitActionEntity>()

    public var inputName: String? = null


    init {
        data.add(HabitFrequencyEntity("Sun"))
        data.add(HabitFrequencyEntity("Mon"))
        data.add(HabitFrequencyEntity("Tue"))
        data.add(HabitFrequencyEntity("Wed"))
        data.add(HabitFrequencyEntity("Thu"))
        data.add(HabitFrequencyEntity("Fri"))
        data.add(HabitFrequencyEntity("Sat"))
        daysOfTheWeekState.value = data

        when (action) {
            is HabitActionEntity.Add -> Unit
            is HabitActionEntity.Edit -> onInitHabitItem(action.id)
        }

        //todo разобраться с типом livedata
        showAction.value = action
        Log.d("MyTag","init ${hashCode()}")
        /*when (action) {
            is HabitActionEntity.Add -> showNewInstanceAddItem.value = action.date
            is HabitActionEntity.Edit -> showNewInstanceEditItem.value = action.id
        }*/
    }

    fun onNameChanged(inputName: String?) {
        this.inputName = inputName
        errorInputNameState.value = false
    }

    fun onSaveClick() {
        when (mainUseCase.actionHabitState(action, inputName)) {
            is MainActionHabitState.Success -> shouldCloseScreenState.value = Unit
            is MainActionHabitState.EmptyNameError -> errorInputNameState.value = true
            is MainActionHabitState.HabitNotFoundError -> shouldCloseScreenState.value = Unit
            is MainActionHabitState.Error -> Unit
        }
    }

    private fun onInitHabitItem(habitItemId: Int) {
        mainUseCase.getHabitItem(action.date,habitItemId)
            ?.let { habit ->
                this.inputName = habit.name
                //habitNameState.value = habit.name
            }
            ?: run { shouldCloseScreenState.value = Unit }
    }

}
