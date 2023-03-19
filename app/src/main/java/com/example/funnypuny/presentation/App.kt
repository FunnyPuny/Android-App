package com.example.funnypuny.presentation

import android.app.Application
import com.example.funnypuny.presentation.di.daoModule
import com.example.funnypuny.presentation.di.repositoryModule
import com.example.funnypuny.presentation.di.useCasesModule
import com.example.funnypuny.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@App)
            // Load modules
            modules(repositoryModule, useCasesModule, viewModelModule, daoModule)
        }
    }
}