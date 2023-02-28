package com.example.funnypuny.domain.entity

data class HabitEntity(
    val name: String,
    val enabled: Boolean,
    var id: Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = -1
    }
}
