package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import io.reactivex.rxjava3.core.Observable

interface HabitListSharedUseCase {

    fun habitsMapSubject(): Observable<Map<DateEntity, List<HabitEntity>>>
    fun getHabitsMap(): Map<DateEntity, List<HabitEntity>>



}