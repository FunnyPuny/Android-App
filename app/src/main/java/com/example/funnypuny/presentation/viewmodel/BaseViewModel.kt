package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel: ViewModel() {
    val disposables = CompositeDisposable()

    /*protected fun addDisposable(disposable: () -> Disposable) {
        disposables.add(disposable.invoke())
    }*/

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        //disposables.clear()
    }
}