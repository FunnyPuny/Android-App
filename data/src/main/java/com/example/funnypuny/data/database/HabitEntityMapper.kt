package com.example.funnypuny.data.database

import android.annotation.SuppressLint
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
    //private val deviceTypeModelMapper by lazy { DeviceTypeModelMapper() }

    @SuppressLint("MissingPermission")
    override fun apply(habitModel: Habit) =
        HabitEntity(
            id = habitModel.id,
            name = habitModel.name,
            enabled = habitModel.enabled,
        )

}