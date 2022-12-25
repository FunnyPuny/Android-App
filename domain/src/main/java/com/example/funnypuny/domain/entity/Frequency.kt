package com.example.funnypuny.domain.entity

/*enum class FrequencyItem(val id: Int, val day: String) {
    SUN(1,"sun"),
    MON(2,"mon"),
    TUE(3,"tue"),
    WED(4,"wed"),
    THU(5,"thu"),
    FRI(6,"fri"),
    SAT(7,"sat"),
    EVERYDAY(0,"everyday")
}*/

data class Frequency(
    val days: String
) {}

/*data class FrequencyItem(
    val sun: String = "sun",
    val mon: String = "mon",
    val tue: String ="tue",
    val wed: String = "wed",
    val thu: String = "thu",
    val fri: String = "fri",
    val sat: String = "sat"
)*/
