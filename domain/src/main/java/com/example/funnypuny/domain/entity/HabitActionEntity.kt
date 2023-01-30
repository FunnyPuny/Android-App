package com.example.funnypuny.domain.entity


sealed class HabitActionEntity {
    object Add: HabitActionEntity()
    data class Edit(val id: Int): HabitActionEntity()
    //data class ChangeEnable(val enabled: Boolean): HabitActionEntity()
}