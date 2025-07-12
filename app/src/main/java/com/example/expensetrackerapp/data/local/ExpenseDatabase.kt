package com.example.expensetrackerapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Expense::class], version = 1, exportSchema = false)
@TypeConverters(EntityTypeConverter::class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}