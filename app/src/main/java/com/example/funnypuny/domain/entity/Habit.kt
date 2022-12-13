package com.example.funnypuny.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit")
data class Habit(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "enabled")
    val enabled: Boolean,
    //val frequency: List<FrequencyItem>,
    @PrimaryKey(autoGenerate = true)
    var id: Int = UNDEFINED_ID
){
    companion object{
        const val UNDEFINED_ID = -1
    }
}
