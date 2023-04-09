package com.example.funnypuny.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.funnypuny.presentation.common.SingleLiveDataEmpty
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel: ViewModel() {
    val disposables = CompositeDisposable()
    val showErrorToast = SingleLiveDataEmpty()
    val progressVisibilityState = MutableLiveData<Boolean>()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}