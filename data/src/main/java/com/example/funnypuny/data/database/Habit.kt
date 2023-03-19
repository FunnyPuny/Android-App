package com.example.funnypuny.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.funnypuny.domain.entity.HabitEntity

@Entity
data class Habit(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "enabled") val enabled: Boolean,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "year") val year: Int
)