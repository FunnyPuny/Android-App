package com.example.funnypuny.domain

data class HabitItem(
    val name: String,
    val enabled: Boolean,
    var id: Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = -1
    }
}
