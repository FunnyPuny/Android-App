package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.data.HabitRepositoryImpl
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.usecases.DeleteHabitItemUseCase
import com.example.funnypuny.domain.usecases.EditHabitItemUseCase
import com.example.funnypuny.domain.interactors.MainInteractor
import com.example.funnypuny.domain.usecases.MainUseCase
import com.example.funnypuny.presentation.common.SingleLiveDataEmpty

class MainViewModel(
    private val mainUseCase: MainUseCase
) : ViewModel() {

    //для всех юзкейсов в качестве параметра конструткора надо передать репозитрой
    // в котором они работают
    //private val repository = HabitRepositoryImpl

    //private val mainInteractor = MainInteractor(repository)

    val habitListState = MutableLiveData<List<HabitEntity>>() //состояние вью, всегда мутабельная лайв дата
    //val showConfirmDeleteDialog = SingleLiveDataEmpty() //какое-то действие, которое нужно сделать один раз

    init {
        habitListState.value = mainUseCase.getHabitList()
    }

    //правильно
    /*fun onDeleteClick() {
        showConfirmDeleteDialog.call()
    }*/

    fun onHabitListScrolled(){

    }

    //неправильно
    fun getHabitList(){

    }

    //----------------


    //private val getHabitListUseCase = GetHabitListUseCase(repository)
    //private val deleteHabitItemUseCase = DeleteHabitItemUseCase(repository)
    //private val editHabitItemUseCase = EditHabitItemUseCase(repository)


    //отображение списка элементов
    // Взаимодействие activity и viewModel должно происходить через LiveData<List<ShopItem>>.
    // В этом случае будет успешно обрабатываться, например, переворот экрана.
    // Если переврнем экран то activity отпищется от объекта LiveData<List<ShopItem>>,
    // экран будет ничтожен, вызовется метод onDestroy(), activity пересоздастся и
    // снова подпишется на объект LiveData<List<ShopItem>>.


    //private val habitRepository = HabitRepository.get()
    //val habitsListLiveData = habitRepository.getAll()


    fun deleteHabitItem(habit: HabitEntity) {
        //deleteHabitItemUseCase.deleteHabitItem(habit)
    }

    fun changeEnableState(habit: HabitEntity) {
        //создаем копию объекта, у которого все поля будут такие же,
        //но состояние enabled будет противоположное
        val newItem = habit.copy(enabled = !habit.enabled)
        //editHabitItemUseCase.editHabitItem(newItem)
    }

}