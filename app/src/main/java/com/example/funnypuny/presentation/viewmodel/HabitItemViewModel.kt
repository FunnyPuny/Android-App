package com.example.funnypuny.presentation.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.R
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.HabitItemFragment
import com.example.funnypuny.presentation.common.SingleLiveData
import com.example.funnypuny.presentation.common.SingleLiveDataEmpty

class HabitItemViewModel(
    private val mainUseCase: MainUseCase
): ViewModel() {

    //private val repository = HabitRepositoryImpl

    //private val getHabitItemUseCase = GetHabitItemUseCase(repository)
    //private val addHabitItemUseCase = AddHabitItemUseCase(repository)
    //private val  editHabitItemUseCase = EditHabitItemUseCase(repository)

   // private val habitRepository = HabitRepository.get()
   // val habitsListLiveData = habitRepository.getAll()

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName


    private val _habitState = MutableLiveData<HabitEntity>()
    val habit: LiveData<HabitEntity>
        get() = _habitState

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val data = ArrayList<HabitFrequencyEntity>()
    val daysOfTheWeekState = MutableLiveData<ArrayList<HabitFrequencyEntity>>()

    val showEditHabitItemFragment = SingleLiveDataEmpty()
    val showAddHabitItemFragment = SingleLiveDataEmpty()

    val showNewInstanceEditItem = SingleLiveDataEmpty()
    val showNewInstanceAddItem = SingleLiveDataEmpty()

    val inputNameState = MutableLiveData<String>()

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
    }

    //todo нужно убрать inputName на вход и сетить его в отдельную переменную по аналогии с методом init
    fun onSaveClick(inputName: String?) {
        when (action) {
            HabitItemAction.ADD -> addHabitItem(inputName)
            HabitItemAction.EDIT -> editHabitItem(inputName)
            null -> Unit
        }
    }

    fun onNameChanged(inputName: String?) {
        this.inputName = inputName
    }

    fun onLaunchRightMode() {
        when (action) {
            HabitItemAction.ADD -> showNewInstanceAddItem.call()
            HabitItemAction.EDIT -> showNewInstanceEditItem.call()
            null -> Unit
        }
    }


    private fun initHabitItem(habitItemId: Int) {
       /* val item = getHabitItemUseCase.getHabitItem(habitItemId)
        _habit.value = item*/
        _habitState.value = mainUseCase.getHabitItem(habitItemId)
    }

    private fun addHabitItem(inputName: String?) {
        val name = parseName(inputName)
        val fieldsValid = validateInput(name)
        if (fieldsValid) {
            val habit = HabitEntity(name,true)
            //addHabitItemUseCase.addHabitItem(habit)
            mainUseCase.addHabitItem(habit)
            finishWork()
        }
    }

    private fun editHabitItem(inputName: String?) {
        val name = parseName(inputName)
        val fieldsValid = validateInput(name)
        if (fieldsValid) {
            _habitState.value?.let {
                val item = it.copy(name = name)
                //editHabitItemUseCase.editHabitItem(item)
                mainUseCase.editHabitItem(item)
                finishWork()
            }
        }
    }

    //обрезаем пробелы
    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    //преобразовываем в число
    //ошибка при вводе НЕчисла, выводим 0
    /*private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }*/

    private fun validateInput(name: String): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }


    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

}

enum class HabitItemAction() {
    ADD,
    EDIT
}