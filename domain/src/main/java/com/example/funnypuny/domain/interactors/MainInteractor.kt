package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.entity.Optional
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.MainActionHabitState
import com.example.funnypuny.domain.usecases.MainChangeHabitState
import com.example.funnypuny.domain.usecases.MainUseCase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class MainInteractor(
    private val habitRepository: HabitRepository
) : MainUseCase {

    override fun changeEnableHabitState(
        date: DateEntity,
        habit: HabitEntity
    ): Observable<MainChangeHabitState> {
        /*val newItem = habit.copy(enabled = !habit.enabled)
        getHabitList(date).let {
            habitRepository.deleteHabitItem(date, habit)
            habitRepository.addHabitItem(date, newItem, null)
        }
        habitRepository.updateHabitsSubject().onNext(Unit)*/
        return habitRepository
            .editHabit(date, habit.copy(enabled = !habit.enabled))
            .doOnComplete { habitRepository.updateHabitsSubject().onNext(Unit) }
            .toSingleDefault<MainChangeHabitState>(MainChangeHabitState.Success)
            .toObservable()
            .onErrorReturn { MainChangeHabitState.Error(it) }
            .startWithItem(MainChangeHabitState.Start)

    }

    override fun deleteHabitItemState(
        date: DateEntity,
        habit: HabitEntity
    ): Observable<MainChangeHabitState> {
        /*habitRepository.deleteHabitItem(date, habit)
        habitRepository.updateHabitsSubject().onNext(Unit)*/
        return habitRepository
            .deleteHabitItem(habit.id)
            .doOnComplete { habitRepository.updateHabitsSubject().onNext(Unit) }
            .toSingleDefault<MainChangeHabitState>(MainChangeHabitState.Success)
            .toObservable()
            .onErrorReturn { MainChangeHabitState.Error(it) }
            .startWithItem(MainChangeHabitState.Start)
    }

    override fun getHabitItem(date: DateEntity, habitItemId: Int): Single<HabitEntity> {
        return habitRepository.getHabitItem(habitItemId)
    }

    override fun actionHabitState(
        action: HabitActionEntity,
        inputName: String?
    ): Observable<MainActionHabitState> =
        when (action) {
            is HabitActionEntity.Add -> addHabitState(action, inputName)
            is HabitActionEntity.Edit -> editHabitState(action, inputName)
        }


    private fun getHabitNameValid(name: String?): Single<Optional<String>> =
        Single.fromCallable {
            Optional(name.takeIf { !it.isNullOrBlank() })
            //name.takeIf { !it.isNullOrBlank() }?.let { Optional.of(it) }?: Optional.empty()
        }

    private fun addHabitState(
        action: HabitActionEntity.Add,
        inputName: String?
    ): Observable<MainActionHabitState> =
        getHabitNameValid(inputName)
            .flatMapObservable { validName ->
                validName.value
                    ?.let { name ->
                        val habit = HabitEntity(name, true)
                        habitRepository
                            .addHabitItem(action.date, habit, null)
                            .doOnComplete { habitRepository.updateHabitsSubject().onNext(Unit) }
                            .toSingleDefault<MainActionHabitState>(MainActionHabitState.Success)
                            .toObservable()
                            .onErrorReturn { MainActionHabitState.Error(it) }
                            .startWithItem(MainActionHabitState.Start)
                    }
                    ?: Observable.just(MainActionHabitState.EmptyNameError)
            }

    private fun editHabitState(
        action: HabitActionEntity.Edit,
        inputName: String?
    ): Observable<MainActionHabitState> =
        getHabitNameValid(inputName)
            .flatMapObservable { validName ->
                validName.value
                    ?.let { name ->
                        habitRepository
                            .getHabitItem(action.id)
                            .flatMapCompletable { habit ->
                                habitRepository.editHabit(action.date, habit.copy(name = name))
                            }
                            .doOnComplete { habitRepository.updateHabitsSubject().onNext(Unit) }
                            .toSingleDefault<MainActionHabitState>(MainActionHabitState.Success)
                            .toObservable()
                            .onErrorReturn { error ->
                                when (error) {
                                    is java.lang.NullPointerException -> MainActionHabitState.HabitNotFoundError
                                    else -> MainActionHabitState.Error(error)
                                }
                            }
                            .startWithItem(MainActionHabitState.Start)
                    }
                    ?: Observable.just(MainActionHabitState.EmptyNameError)
            }
    /*if (isHabitNameValid(inputName)) {
        inputName?.let { name ->
            habitRepository.getHabitItem(action.date, action.id)
                ?.let { habit ->
                    val indexPosition = getHabitList(action.date).indexOf(habit)
                        .takeIf { it != -1 }
                    habitRepository.deleteHabitItem(action.date, habit)
                    habitRepository.addHabitItem(
                        date = action.date,
                        habit = habit.copy(name = name),
                        indexPosition = indexPosition
                    )
                    return MainActionHabitState.Success
                }
                ?: return MainActionHabitState.HabitNotFoundError
        }
    }*/
    //return MainActionHabitState.EmptyNameError


    //private fun getHabitList(date: DateEntity): List<HabitEntity> = habitRepository.getHabitMap()[date]?: emptyList()

}