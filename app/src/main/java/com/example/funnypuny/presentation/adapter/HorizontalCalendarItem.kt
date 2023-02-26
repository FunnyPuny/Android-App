package com.example.funnypuny.presentation.adapter

import java.util.Date

data class HorizontalCalendarItem(
    var isSelected: Boolean,
    val dayOfTheWeek: String,
    val dayOfTheMonth: Int
)