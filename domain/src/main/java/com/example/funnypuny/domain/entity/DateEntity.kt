package com.example.funnypuny.domain.entity

data class DateEntity(
    val day: Int,
    val month: Int,
    val year: Int
)

enum class WeekEntity(
    val day: Int
) {
    MON(1),
    TUE(2),
    WED(3)
}