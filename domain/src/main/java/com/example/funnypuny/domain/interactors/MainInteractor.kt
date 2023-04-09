package com.example.funnypuny.domain.interactors

import com.example.funnypuny.domain.entity.DateEntity
import com.example.funnypuny.domain.entity.HabitEntity
import com.example.funnypuny.domain.entity.HabitActionEntity
import com.example.funnypuny.domain.entity.Optional
import com.example.funnypuny.domain.repository.HabitGetHabitItemState
import com.example.funnypuny.domain.repository.HabitRepository
import com.example.funnypuny.domain.usecases.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

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

    override fun getHabitItem(
        date: DateEntity,
        habitItemId: Int
    ): Observable<MainGetHabitItemState> {
        return habitRepository
            .getHabitItem(habitItemId)
            .map { state ->
                when (state) {
                    is HabitGetHabitItemState.Error -> MainGetHabitItemState.Error(state.error)
                    is HabitGetHabitItemState.HabitNotFoundError -> MainGetHabitItemState.HabitNotFoundError
                    is HabitGetHabitItemState.Success -> MainGetHabitItemState.Success(state.habit)
                }
            }
            .toObservable()
            .startWithItem(MainGetHabitItemState.Start)
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
                            .addHabitItem(action.date, habit)
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
                            .flatMapObservable { state ->
                                when (state) {
                                    is HabitGetHabitItemState.Error ->
                                        Observable.just(MainActionHabitState.Error(state.error))
                                    is HabitGetHabitItemState.HabitNotFoundError ->
                                        Observable.just(MainActionHabitState.HabitNotFoundError)
                                    is HabitGetHabitItemState.Success ->
                                        habitRepository
                                            .editHabit(action.date, state.habit.copy(name = name))
                                            .doOnComplete {
                                                habitRepository.updateHabitsSubject().onNext(Unit)
                                            }
                                            .toSingleDefault<MainActionHabitState>(
                                                MainActionHabitState.Success
                                            )
                                            .toObservable()
                                            .onErrorReturn { MainActionHabitState.Error(it) }
                                }
                            }
                            .startWithItem(MainActionHabitState.Start)
                    }
                    ?: Observable.just(MainActionHabitState.EmptyNameError)
            }

}