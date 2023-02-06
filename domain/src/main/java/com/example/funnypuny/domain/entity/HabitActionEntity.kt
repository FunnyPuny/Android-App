package com.example.funnypuny.domain.entity


sealed class HabitActionEntity {

    abstract val date: DateEntity
    data class Add(override val date: DateEntity): HabitActionEntity()
    data class Edit(override val date: DateEntity, val id: Int): HabitActionEntity()
}