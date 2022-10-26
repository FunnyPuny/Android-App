package com.example.funnypuny.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.data.HabbitListRepositoryImpl
import com.example.funnypuny.domain.*
import kotlinx.coroutines.flow.merge

class MainViewModel: ViewModel() {

    //для всех юзкейсов в качестве параметра конструткора надо передать репозитрой
    // в котором они работают
    private val repository = HabbitListRepositoryImpl

    private val getHabbitListUseCase = GetHabbitListUseCase(repository)
    private val deleteHabbitItemUseCase = DeleteHabbitItemUseCase(repository)
    private val editHabbitItemUseCase = EditHabbitItemUseCase(repository)

    //отображение списка элементов
    // Взаимодействие activity и viewModel должно происходить через LiveData<List<ShopItem>>.
    // В этом случае будет успешно обрабатываться, например, переворот экрана.
    // Если переврнем экран то activity отпищется от объекта LiveData<List<ShopItem>>,
    // экран будет ничтожен, вызовется метод onDestroy(), activity пересоздастся и
    // снова подпишется на объект LiveData<List<ShopItem>>.

    val habbitList = MutableLiveData<List<HabbitItem>>()

    fun getHabbitList() {
        val list = getHabbitListUseCase.getHabbbitList()
        habbitList.value = list
    }

    fun deleteHabbitItem(habbitItem: HabbitItem) {
        deleteHabbitItemUseCase.deleteHabbitItem(habbitItem)
        getHabbitList()
    }

    fun changeEnabledState(habbitItem: HabbitItem) {

        //создаем копию объекта, у которого все поля будут такие же,
        //но состояние enabled будет противоположное
        val newItem = habbitItem.copy(enabled = !habbitItem.enabled)
        editHabbitItemUseCase.editHabbitItem(newItem)
        getHabbitList()
    }

}