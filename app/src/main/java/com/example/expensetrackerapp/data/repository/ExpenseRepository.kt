package com.example.expensetrackerapp.data.repository

import com.example.expensetrackerapp.data.local.Expense
import com.example.expensetrackerapp.data.local.ExpenseDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class ExpenseRepository @Inject constructor(private val dao: ExpenseDao) {

    suspend fun addExpense(expense: Expense) = dao.insertExpense(expense)

    fun getExpensesForDate(date: LocalDate): Flow<List<Expense>> = dao.getExpensesByDate(date)

    fun getAllExpenses() = dao.getAllExpenses()
}