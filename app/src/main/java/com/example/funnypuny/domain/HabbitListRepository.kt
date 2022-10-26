package com.example.funnypuny.domain

import androidx.lifecycle.LiveData

interface HabbitListRepository {

    fun addHabbitItem(habbitItem: HabbitItem)

    fun editHabbitItem(habbitItem: HabbitItem)

    fun deleteHabbitItem(habbitItem: HabbitItem)

    fun getHabbitItem(habbitItemId: Int): HabbitItem

    fun getHabbitList(): LiveData<List<HabbitItem>>

}