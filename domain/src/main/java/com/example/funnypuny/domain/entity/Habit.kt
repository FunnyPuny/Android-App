package com.example.funnypuny.domain.entity

data class Habit(
    val name: String,
    val enabled: Boolean,
    //val frequency: List<FrequencyItem>,
    var id: Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = -1
    }
}
