package com.example.funnypuny.domain

data class HabbitItem(
    val id: Int = UNDEFINED_ID,
    val name: String,
    val enabled: Boolean
){
    companion object{
        const val UNDEFINED_ID = -1
    }
}
