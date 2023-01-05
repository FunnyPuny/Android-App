package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HorizontalCalendarViewModel: ViewModel() {

    val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH)
    val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    val cal = Calendar.getInstance(Locale.ENGLISH)

    // current date
    val currentDate = Calendar.getInstance(Locale.ENGLISH)
    val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    val currentMonth = currentDate[Calendar.MONTH]
    val currentYear = currentDate[Calendar.YEAR]

    // selected date
    var selectedDay: Int = currentDay
    var selectedMonth: Int = currentMonth
    var selectedYear: Int = currentYear

    // all days in month
    val dates = ArrayList<Date>()

    fun onPrevButtonClick() {
        if (cal.after(currentDate)) {
            cal.add(Calendar.MONTH, -1)
            if (cal == currentDate)
                setUpCalendar()
            else
                setUpCalendar(changeMonth = cal)
        }
    }

    fun onNextButtonClick() {
        if (cal.before(lastDayInCalendar)) {
            cal.add(Calendar.MONTH, 1)
            setUpCalendar(changeMonth = cal)
        }
    }


}