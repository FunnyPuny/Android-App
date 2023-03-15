package com.example.funnypuny.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.presentation.common.SingleLiveData

class HabitItemActivityViewModel(
    private val action: HabitActionEntity
) : ViewModel() {

    val actionState = SingleLiveData<HabitActionEntity>()

    init {
        actionState.value = action
        Log.d("MyTag","init ${hashCode()}")
    }

}
