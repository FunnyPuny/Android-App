package com.example.funnypuny.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.usecases.MainActionHabitState
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.common.SingleLiveData

class HabitItemActivityViewModel(
    private val action: HabitActionEntity,
    private val mainUseCase: MainUseCase
) : ViewModel() {

    val shouldCloseScreenState = MutableLiveData<Unit>()

    val showAction = SingleLiveData<HabitActionEntity>()

    public var inputName: String? = null


    init {

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

    private fun onInitHabitItem(habitItemId: Int) {
        mainUseCase.getHabitItem(action.date,habitItemId)
            ?.let { habit ->
                this.inputName = habit.name
                //habitNameState.value = habit.name
            }
            ?: run { shouldCloseScreenState.value = Unit }
    }

}
