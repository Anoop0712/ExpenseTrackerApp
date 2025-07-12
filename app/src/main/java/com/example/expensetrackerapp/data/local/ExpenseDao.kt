package com.example.expensetrackerapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE date = :date")
    fun getExpensesByDate(date: LocalDate): Flow<List<Expense>>

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): Flow<List<Expense>>
}