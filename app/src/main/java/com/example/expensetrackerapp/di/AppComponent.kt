package com.example.expensetrackerapp.di

import android.app.Application
import com.example.expensetrackerapp.ExpenseTrackerApp
import com.example.expensetrackerapp.presentation.ExpenseViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class])
interface AppComponent {

    fun inject(app: ExpenseTrackerApp)
    fun inject(viewModel: ExpenseViewModel)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}