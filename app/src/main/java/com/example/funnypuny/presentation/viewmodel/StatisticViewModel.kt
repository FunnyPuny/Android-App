package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.StatisticUseCase
import com.example.funnypuny.presentation.common.SingleLiveData

class StatisticViewModel(
    private val statisticUseCase: StatisticUseCase
): ViewModel() {

    val habitListState = MutableLiveData<List<HabitEntity>>()
    val showHabitSpinner = SingleLiveData<Int>()

    var action: HabitItemAction? = null
    var id: Int? = null
    var isAlreadyInited = false
    var inputName: String? = null

    init {
        habitListState.value = statisticUseCase.getHabitList()
    }

    fun onHabitSpinner(id:Int){
        showHabitSpinner.value = id
    }


    /*fun init(id: Int) {
        if (isAlreadyInited) return
        //this.action = action
        this.id = id

        onInitHabitItem(id)

        isAlreadyInited = true

    }*/

    /*fun onInitHabitItem(habitItemId: Int) {
        habitListState.value = statisticUseCase.getHabitItem(habitItemId)
    }*/


}
enum class HabitAction() {
    ADD,
    EDIT
}