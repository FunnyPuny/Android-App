package com.example.funnypuny.presentation

import androidx.lifecycle.ViewModel
import com.example.funnypuny.data.HabitListRepositoryImpl
import com.example.funnypuny.domain.*

class MainViewModel: ViewModel() {

    //для всех юзкейсов в качестве параметра конструткора надо передать репозитрой
    // в котором они работают
    private val repository = HabitListRepositoryImpl

    private val getHabitListUseCase = GetHabitListUseCase(repository)
    private val deleteHabitItemUseCase = DeleteHabitItemUseCase(repository)
    private val editHabitItemUseCase = EditHabitItemUseCase(repository)

    //отображение списка элементов
    // Взаимодействие activity и viewModel должно происходить через LiveData<List<ShopItem>>.
    // В этом случае будет успешно обрабатываться, например, переворот экрана.
    // Если переврнем экран то activity отпищется от объекта LiveData<List<ShopItem>>,
    // экран будет ничтожен, вызовется метод onDestroy(), activity пересоздастся и
    // снова подпишется на объект LiveData<List<ShopItem>>.

    val habitList = getHabitListUseCase.getHabitList()


    fun deleteHabitItem(habitItem: HabitItem) {
        deleteHabitItemUseCase.deleteHabitItem(habitItem)
    }

    fun changeEnableState(habitItem: HabitItem) {
        //создаем копию объекта, у которого все поля будут такие же,
        //но состояние enabled будет противоположное
        val newItem = habitItem.copy(enabled = !habitItem.enabled)
        editHabitItemUseCase.editHabitItem(newItem)
    }

}