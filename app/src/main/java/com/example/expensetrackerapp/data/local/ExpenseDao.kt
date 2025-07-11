package com.example.expensetrackerapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE date(timestamp / 1000, 'unixepoch') = date(:date / 1000, 'unixepoch')")
    suspend fun getExpensesForDate(date: Long): List<Expense>

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    suspend fun getAllExpenses(): List<Expense>

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT SUM(amount) FROM expenses WHERE date(timestamp / 1000, 'unixepoch') = date(:date / 1000, 'unixepoch')")
    suspend fun getTotalForDate(date: Long): Double
}