package com.example.funnypuny.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.presentation.common.SingleLiveData

class HabitItemActivityViewModel(
    private val action: HabitActionEntity
) : ViewModel() {

    //val shouldCloseScreenState = MutableLiveData<Unit>()

    val actionState = SingleLiveData<HabitActionEntity>()

    //public var inputName: String? = null


    init {

        /*when (action) {
            is HabitActionEntity.Add -> Unit
            is HabitActionEntity.Edit -> onInitHabitItem(action.id)
        }*/

        actionState.value = action
        Log.d("MyTag","init ${hashCode()}")
    }

    /*private fun onInitHabitItem(habitItemId: Int) {
        mainUseCase.getHabitItem(action.date,habitItemId)
            ?.let { habit ->
                this.inputName = habit.name
                //habitNameState.value = habit.name
            }
            ?: run { shouldCloseScreenState.value = Unit }
    }*/

}
