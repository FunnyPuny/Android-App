package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.funnypuny.data.HabitListRepositoryImpl
import com.example.funnypuny.domain.entity.Habit
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.DeleteHabitItemUseCase
import com.example.funnypuny.domain.usecases.EditHabitItemUseCase
import com.example.funnypuny.domain.usecases.GetHabitListUseCase

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

    private val habitRepository = HabitRepository.get()
    val habitsListLiveData = habitRepository.getAll()


    fun deleteHabitItem(habit: Habit) {
        deleteHabitItemUseCase.deleteHabitItem(habit)
    }

    fun changeEnableState(habit: Habit) {
        //создаем копию объекта, у которого все поля будут такие же,
        //но состояние enabled будет противоположное
        val newItem = habit.copy(enabled = !habit.enabled)
        editHabitItemUseCase.editHabitItem(newItem)
    }

}