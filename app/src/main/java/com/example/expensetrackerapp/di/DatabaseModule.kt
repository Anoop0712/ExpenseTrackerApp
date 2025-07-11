package com.example.expensetrackerapp.di

import android.content.Context
import androidx.room.Room
import com.example.expensetrackerapp.data.local.ExpenseDao
import com.example.expensetrackerapp.data.local.ExpenseDatabase
import com.example.expensetrackerapp.data.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(db: ExpenseDatabase): ExpenseDao {
        return db.expenseDao()
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(dao: ExpenseDao): ExpenseRepository {
        return ExpenseRepository(dao)
    }
}
