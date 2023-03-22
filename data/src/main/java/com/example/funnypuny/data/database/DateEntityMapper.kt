package com.example.funnypuny.data.database

import android.annotation.SuppressLint
import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import io.reactivex.rxjava3.functions.Function

class DateEntityMapper: Function<Habit, DateEntity> {
    //private val deviceTypeModelMapper by lazy { DeviceTypeModelMapper() }

    @SuppressLint("MissingPermission")
    override fun apply(habitModel: Habit) =
        DateEntity(
            day = habitModel.day,
            month = habitModel.month,
            year = habitModel.year
        )

}