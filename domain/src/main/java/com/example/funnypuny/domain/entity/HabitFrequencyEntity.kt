package com.example.funnypuny.domain.entity

data class HabitFrequencyEntity(
    val days: String
)

enum class DayOfWeek(val id: Int) {
    EVERYDAY(0),
    SUN(1),
    MON(2),
    TUE(3),
    WED(4),
    THU(5),
    FRI(6),
    SAT(7)
}
