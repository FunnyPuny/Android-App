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
    val shouldCloseScreenState = MutableLiveData<Unit>()
    val daysOfTheWeekState = MutableLiveData<ArrayList<HabitFrequencyEntity>>()

    //todo переписать на livedata
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

        //todo разобраться с типом livedata
        Log.d("MyTag", "init ${hashCode()}")
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
            is MainActionHabitState.Success -> shouldCloseScreenState.value = Unit
            is MainActionHabitState.EmptyNameError -> errorInputNameState.value = true
            is MainActionHabitState.HabitNotFoundError -> shouldCloseScreenState.value = Unit
            is MainActionHabitState.Error -> showErrorToast.call()
            is MainActionHabitState.Start -> Log.d("HabitItemViewModel", "MainActionHabitState.Start")
        }
    }

    private fun handleActionHabitItemState(state: MainGetHabitItemState) {
        when (state) {
            is MainGetHabitItemState.Error -> showErrorToast.call()
            is MainGetHabitItemState.HabitNotFoundError -> shouldCloseScreenState.value = Unit
            is MainGetHabitItemState.Start -> Log.d("HabitItemViewModel", "GetHabitItem = Start")
            is MainGetHabitItemState.Success -> {
                this.inputName = state.habit.name
                initInputName.value = state.habit.name
            }
        }
    }



    private fun onInitHabitItem(habitItemId: Int) {
        mainUseCase
            .getHabitItem(action.date, 1000000)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{ Log.d("HabitItemViewModel", "GetHabitItem = Start") }
            .subscribe{handleActionHabitItemState(it)}
            .also { disposables.add(it) }
    }

}
