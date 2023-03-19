package com.example.funnypuny.domain.usecases

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import io.reactivex.rxjava3.core.Observable

interface HabitListSharedUseCase {

    fun habitsSubject(date: DateEntity): Observable<List<HabitEntity>>

}