package com.example.funnypuny.presentation.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.R
import com.example.funnypuny.data.HabitRepositoryImpl
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.HabitItemFragment
import com.example.funnypuny.presentation.common.SingleLiveData
import com.example.funnypuny.presentation.common.SingleLiveDataEmpty
import com.example.funnypuny.presentation.view.HabitItemActivity

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


    private val _habit = MutableLiveData<HabitEntity>()
    val habit: LiveData<HabitEntity>
        get() = _habit

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val data = ArrayList<HabitFrequencyEntity>()
    val daysOfTheWeekState = MutableLiveData<ArrayList<HabitFrequencyEntity>>()

    val showEditHabitItemFragment = SingleLiveDataEmpty()
    val showAddHabitItemFragment = SingleLiveDataEmpty()

    val showException = SingleLiveData<String>()


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

    fun onShowRuntimeException(exception: String){
        showException.value = exception
    }

    fun onEditHabitItemFragment() {
        showEditHabitItemFragment.call()
    }

    fun onAddHabitItemFragment() {
        showAddHabitItemFragment.call()
    }


    fun getHabitItem(habitItemId: Int) {
       /* val item = getHabitItemUseCase.getHabitItem(habitItemId)
        _habit.value = item*/
        _habit.value = mainUseCase.getHabitItem(habitItemId)
    }

    fun addHabitItem(inputName: String?) {
        val name = parseName(inputName)
        val fieldsValid = validateInput(name)
        if (fieldsValid) {
            val habit = HabitEntity(name,true)
            //addHabitItemUseCase.addHabitItem(habit)
            mainUseCase.addHabitItem(habit)
            finishWork()
        }
    }

    fun editHabitItem(inputName: String?) {
        val name = parseName(inputName)
        val fieldsValid = validateInput(name)
        if (fieldsValid) {
            _habit.value?.let {
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