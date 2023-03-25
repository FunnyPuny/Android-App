package com.example.funnypuny.data.database

import android.annotation.SuppressLint
import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import io.reactivex.rxjava3.functions.Function

/*class DeviceModelMapper : Function<BluetoothDevice, DeviceModel> {
    private val deviceTypeModelMapper by lazy { DeviceTypeModelMapper() }

    @SuppressLint("MissingPermission")
    override fun apply(apiModel: BluetoothDevice) =
        DeviceModel(
            type = deviceTypeModelMapper.apply(apiModel.name),
            mac = apiModel.address,
            name = apiModel.name,
            signal = null
        )
}*/

class HabitEntityMapper : Function<Habit, HabitEntity> {
    override fun apply(habitModel: Habit): HabitEntity =
        HabitEntity(
            id = habitModel.id,
            name = habitModel.name,
            enabled = habitModel.enabled,
        )
}

class HabitMapper : Function<Pair<DateEntity,HabitEntity>, Habit> {
    override fun apply(info: Pair<DateEntity,HabitEntity>): Habit =
        info.let {(date, habit) ->
            Habit(
                id = habit.id,
                name = habit.name,
                enabled = habit.enabled,
                day = date.day,
                month = date.month,
                year = date.year
            )
        }
}

class HabitsEntityMapper : Function<List<Habit>, List<HabitEntity>> {
    private val habitEntityMapper by lazy { HabitEntityMapper() }
    override fun apply(habits: List<Habit>): List<HabitEntity> =
        habits.map { habitEntityMapper.apply(it) }
}