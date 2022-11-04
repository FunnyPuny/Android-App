package com.example.funnypuny.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.data.HabitListRepositoryImpl
import com.example.funnypuny.domain.*

class HabitItemViewModel: ViewModel() {

    private val repository = HabitListRepositoryImpl

    private val getHabitItemUseCase = GetHabitItemUseCase(repository)
    private val addHabitItemUseCase = AddHabitItemUseCase(repository)
    private val  editHabitItemUseCase = EditHabitItemUseCase(repository )

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<HabitItem>()
    val shopItem: LiveData<HabitItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getHabitItem(habitItemId: Int) {
        val item = getHabitItemUseCase.getHabitItem(habitItemId )
    }

    fun addHabitItem(inputName: String?) {
        val name = parseName(inputName)
        val fieldsValid = validateInput(name)
        if (fieldsValid) {
            val shopItem = HabitItem(name,true)
            addHabitItemUseCase.addHabitItem(shopItem)
            finishWork()
        }
    }

    fun editHabitItem(habitItem: HabitItem  ) {
        editHabitItemUseCase.editHabitItem(habitItem)
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

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

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

}