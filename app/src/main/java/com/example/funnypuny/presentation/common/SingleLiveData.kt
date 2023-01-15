package com.example.funnypuny.presentation.common

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveData<T> : MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }
}

class SingleLiveDataEmpty : SingleLiveData<Unit>() {
    @MainThread
    fun call() {
        value = Unit
    }

    // only for background thread
    fun postCall() {
        postValue(Unit)
    }
}

class SingleLiveDataOnce : SingleLiveData<Unit>() {

    private var called = false

    @MainThread
    fun call() {
        if (called.not()) {
            called = true
            value = Unit
        }

    }

    // only for background thread
    fun postCall() {
        if (called.not()) {
            called = true
            postValue(Unit)
        }
    }
}

open class SingleLiveDataDistinct<T> : SingleLiveData<T>() {

    @MainThread
    override fun setValue(t: T?) {
        if (t == value) return
        super.setValue(t)
    }
}