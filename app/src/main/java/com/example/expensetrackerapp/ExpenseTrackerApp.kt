package com.example.expensetrackerapp

import android.app.Application
import com.example.expensetrackerapp.di.AppComponent
import com.example.expensetrackerapp.di.DaggerAppComponent

class ExpenseTrackerApp : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}