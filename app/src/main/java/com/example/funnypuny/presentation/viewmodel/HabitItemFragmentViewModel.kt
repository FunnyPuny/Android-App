package com.example.funnypuny.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.HabitListSharedUseCase
import com.example.funnypuny.domain.usecases.MainActionHabitState
import com.example.funnypuny.domain.usecases.MainChangeHabitState
import com.example.funnypuny.domain.usecases.MainUseCase
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

    private fun onInitHabitItem(habitItemId: Int) {
        mainUseCase
            .getHabitItem(action.date, habitItemId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{ Log.d("HabitItemViewModel", "GetHabitItem = Start") }
            .subscribe(
                { habit ->
                    this.inputName = habit.name
                },
                {error ->
                    //shouldCloseScreenState.value = Unit
                    when (error) {
                        is java.lang.NullPointerException -> shouldCloseScreenState.value = Unit
                        //todo показать toast
                        else -> Unit
                    }
                })
            .also { disposables.add(it) }
    }

}
