package com.example.funnypuny.data

import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitFrequencyEntity
import com.example.funnypuny.domain.repository.HabitRepository

class HabitRepositoryImpl : HabitRepository {

    // private val habitListLD = MutableLiveData<List<Habit>>()
    //храним все в переменной, в которой будем хранить коллекцию эллементов
    private val habitList = mutableListOf<HabitEntity>()

    //переменная которая будет хранить id элементов
    private var autoIncrementId = 0


    init {
        for (i in 0 until 3) {
            addHabitItem(HabitEntity("Name: $i", true, i))
        }
    }

    override fun getHabitList(): List<HabitEntity> {
        return habitList
    }


    //----
    override fun addHabitItem(habit: HabitEntity) {
        //при редоктировании элемента надо сохранить его id
        //если id данного элемента неопределен, то в этом случае мы его установим
        if (habit.id == HabitEntity.UNDEFINED_ID) {
            habit.id = autoIncrementId++
        }
        habitList.add(habit)
        //updateList()

    }

    override fun editHabitItem(habit: HabitEntity): List<HabitEntity> {
        // нужно удалить старый объект и положить новый
        // но в кач-ве параметра прилетает уже новый объект с измененными полями
        // т.е мы не можем удалить его из коллекции - элемент найден не будет

        // поэтому надо сначала найти старый элемент по его id - удалить и добавить новый
        val oldElement = getHabitItem(habit.id)
        habitList.remove(oldElement)
        addHabitItem(habit)
        return habitList
    }

    override fun deleteHabitItem(habit: HabitEntity): List<HabitEntity> {
        habitList.remove(habit)
        return habitList
    }

    override fun getHabitItem(habitItemId: Int): HabitEntity {
        // ищем элемент по id и возвращаем его
        // если вдруг элемент не найден, приложение упадет с исключением
        return habitList.find {
            it.id == habitItemId
        } ?: throw RuntimeException("Element with id $habitItemId not found")
    }


    //обновление лайвдвты
    private fun updateList() {
        //habitListLD.value = habitList.toList()
    }
}