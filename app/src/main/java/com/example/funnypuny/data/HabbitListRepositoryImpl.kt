package com.example.funnypuny.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.funnypuny.domain.HabbitItem
import com.example.funnypuny.domain.HabbitListRepository
import java.lang.RuntimeException

object HabbitListRepositoryImpl: HabbitListRepository {

    private val habbitListLD = MutableLiveData<List<HabbitItem>>()
    //храним все в переменной, в которой будем хранить коллекцию эллементов
    private val habbitList = mutableListOf<HabbitItem>()

    //переменная которая будет хранить id элементов
    private var autoIncrementId = 0

    init {
        for (i in 0 until 10){
            val item = HabbitItem(i,"Name: $i",true)
            addHabbitItem(item)
        }
    }

    override fun addHabbitItem(habbitItem: HabbitItem) {
        //при редоктировании элемента надо сохранить его id
        //если id данного элемента неопределен, то в этом случае мы его установим
        if (habbitItem.id == HabbitItem.UNDEFINED_ID){
            habbitItem.id = autoIncrementId++
        }
        habbitList.add(habbitItem)
        updateList()
    }

    override fun editHabbitItem(habbitItem: HabbitItem) {
        // нужно удалить старый объект и положить новый
        // но в кач-ве параметра прилетает уже новый объект с измененными полями
        // т.е мы не можем удалить его из коллекции - элемент найден не будет

        // поэтому надо сначала найти старый элемент по его id - удалить и добавить новый
        val oldElement = getHabbitItem(habbitItem.id)
        habbitList.remove(oldElement)
        addHabbitItem(habbitItem)
    }

    override fun deleteHabbitItem(habbitItem: HabbitItem) {
        habbitList.remove(habbitItem)
        updateList()
    }

    override fun getHabbitItem(habbitItemId: Int): HabbitItem {
        // ищем элемент по id и возвращаем его
        // если вдруг элемент не найден, приложение упадет с исключением
        return habbitList.find {
            it.id == habbitItemId
        } ?: throw RuntimeException("Element with id $habbitItemId not found")
    }

    override fun getHabbitList(): LiveData<List<HabbitItem>> {
        // лучше возвращать копию листа
        // если в getShopList() добавлять какие-то элементы или удалять их,
        // то на исходную коллекцию это никак не повлияет
        return habbitListLD
    }

    //обновление лайвдвты
    private fun updateList(){
        habbitListLD.value = habbitList.toList()
    }
}