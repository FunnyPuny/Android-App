package com.example.funnypuny.domain

interface HabbitListRepository {

    fun addHabbitItem(habbitItem: HabbitItem)

    fun editHabbitItem(habbitItem: HabbitItem)

    fun deleteHabbitItem(habbitItem: HabbitItem)

    fun getHabbitItem(habbitItemId: Int): HabbitItem

    fun getHabbitList(): List<HabbitItem>

}