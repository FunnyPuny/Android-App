package com.example.funnypuny.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.funnypuny.domain.HabitItem
import com.example.funnypuny.domain.HabitListRepository
import java.lang.RuntimeException

object HabitListRepositoryImpl: HabitListRepository {

    private val habitListLD = MutableLiveData<List<HabitItem>>()
    //храним все в переменной, в которой будем хранить коллекцию эллементов
    private val habitList = mutableListOf<HabitItem>()

    //переменная которая будет хранить id элементов
    private var autoIncrementId = 0

    init {
        for (i in 0 until 5){
            val item = HabitItem(i,"Name: $i",true)
            addHabitItem(item)
        }
    }

    override fun addHabitItem(habitItem: HabitItem) {
        //при редоктировании элемента надо сохранить его id
        //если id данного элемента неопределен, то в этом случае мы его установим
        if (habitItem.id == HabitItem.UNDEFINED_ID){
            habitItem.id = autoIncrementId++
        }
        habitList.add(habitItem)
        updateList()
    }

    override fun editHabitItem(habitItem: HabitItem) {
        // нужно удалить старый объект и положить новый
        // но в кач-ве параметра прилетает уже новый объект с измененными полями
        // т.е мы не можем удалить его из коллекции - элемент найден не будет

        // поэтому надо сначала найти старый элемент по его id - удалить и добавить новый
        val oldElement = getHabitItem(habitItem.id)
        habitList.remove(oldElement)
        addHabitItem(habitItem)
    }

    override fun deleteHabitItem(habitItem: HabitItem) {
        habitList.remove(habitItem)
        updateList()
    }

    override fun getHabitItem(habitItemId: Int): HabitItem {
        // ищем элемент по id и возвращаем его
        // если вдруг элемент не найден, приложение упадет с исключением
        return habitList.find {
            it.id == habitItemId
        } ?: throw RuntimeException("Element with id $habitItemId not found")
    }

    override fun getHabitList(): LiveData<List<HabitItem>> {
        // лучше возвращать копию листа
        // если в getShopList() добавлять какие-то элементы или удалять их,
        // то на исходную коллекцию это никак не повлияет
        return habitListLD
    }

    //обновление лайвдвты
    private fun updateList(){
        habitListLD.value = habitList.toList()
    }
}