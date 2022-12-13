package com.example.funnypuny.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.funnypuny.domain.entity.FrequencyItem
import com.example.funnypuny.domain.entity.Habit
import com.example.funnypuny.domain.repository.HabitListRepository
import java.lang.RuntimeException

object HabitListRepositoryImpl: HabitListRepository {

    private val habitListLD = MutableLiveData<List<Habit>>()
    //храним все в переменной, в которой будем хранить коллекцию эллементов
    private val habitList = mutableListOf<Habit>()

    private val dayListLD = MutableLiveData<List<FrequencyItem>>()
    private val dayList = mutableListOf<FrequencyItem>()

    //переменная которая будет хранить id элементов
    private var autoIncrementId = 0

    /*init {
        for (i in 0 until 5){
            val item = HabitItem("Name: $i",true,i)
            addHabitItem(item)
        }
    }*/

    override fun addHabitItem(habit: Habit) {
        //при редоктировании элемента надо сохранить его id
        //если id данного элемента неопределен, то в этом случае мы его установим
        if (habit.id == Habit.UNDEFINED_ID){
            habit.id = autoIncrementId++
        }
        habitList.add(habit)
        updateList()
    }

    override fun editHabitItem(habit: Habit) {
        // нужно удалить старый объект и положить новый
        // но в кач-ве параметра прилетает уже новый объект с измененными полями
        // т.е мы не можем удалить его из коллекции - элемент найден не будет

        // поэтому надо сначала найти старый элемент по его id - удалить и добавить новый
        val oldElement = getHabitItem(habit.id)
        habitList.remove(oldElement)
        addHabitItem(habit)
    }

    override fun deleteHabitItem(habit: Habit) {
        habitList.remove(habit)
        updateList()
    }

    override fun getHabitItem(habitItemId: Int): Habit {
        // ищем элемент по id и возвращаем его
        // если вдруг элемент не найден, приложение упадет с исключением
        return habitList.find {
            it.id == habitItemId
        } ?: throw RuntimeException("Element with id $habitItemId not found")
    }

    override fun getHabitList(): LiveData<List<Habit>> {
        // лучше возвращать копию листа
        // если в getShopList() добавлять какие-то элементы или удалять их,
        // то на исходную коллекцию это никак не повлияет
        return habitListLD
    }

    override fun getDayList(): LiveData<List<FrequencyItem>> {
        return dayListLD
    }

    //обновление лайвдвты
    private fun updateList(){
        habitListLD.value = habitList.toList()
        dayListLD.value = dayList.toList()
    }
}