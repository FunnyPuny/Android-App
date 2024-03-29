package com.example.funnypuny.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.usecases.*
import com.example.funnypuny.presentation.common.SingleLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HabitItemFragmentViewModel(
    private val action: HabitActionEntity,
    private val mainUseCase: MainUseCase
) : BaseViewModel() {

    val errorInputNameState = MutableLiveData<Boolean>()
    val closeScreen = SingleLiveData<Unit>()
    val daysOfTheWeekState = MutableLiveData<ArrayList<HabitFrequencyEntity>>()

    var inputName: String? = null
    val initInputName = SingleLiveData<String>()
    private val data = ArrayList<HabitFrequencyEntity>()


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
    }

    fun onNameChanged(inputName: String?) {
        this.inputName = inputName
        errorInputNameState.value = false
    }

    fun onSaveClick() {
        mainUseCase
            .actionHabitState(action, inputName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleActionHabitState(it) }
            .also { disposables.add(it) }

    }

    private fun handleActionHabitState(state: MainActionHabitState) {
        when (state) {
            is MainActionHabitState.Success -> {
                progressVisibilityState.value = false
                closeScreen.value = Unit
            }
            is MainActionHabitState.EmptyNameError -> errorInputNameState.value = true
            is MainActionHabitState.HabitNotFoundError -> {
                showErrorToast.call()
                closeScreen.value = Unit
            }
            is MainActionHabitState.Error -> {
                progressVisibilityState.value = false
                showErrorToast.call()
            }
            is MainActionHabitState.Start -> progressVisibilityState.value = true
        }
    }

    private fun handleGetHabitItemState(state: MainGetHabitItemState) {
        when (state) {
            is MainGetHabitItemState.Start -> progressVisibilityState.value = true
            is MainGetHabitItemState.Success -> {
                progressVisibilityState.value = false
                this.inputName = state.habit.name
                initInputName.value = state.habit.name
            }
            is MainGetHabitItemState.Error -> {
                progressVisibilityState.value = false
                showErrorToast.call()
            }
            is MainGetHabitItemState.HabitNotFoundError -> {
                showErrorToast.call()
                closeScreen.value = Unit
            }
        }
    }



    private fun onInitHabitItem(habitItemId: Int) {
        mainUseCase
            .getHabitItem(action.date, habitItemId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{handleGetHabitItemState(it)}
            .also { disposables.add(it) }
    }

}
