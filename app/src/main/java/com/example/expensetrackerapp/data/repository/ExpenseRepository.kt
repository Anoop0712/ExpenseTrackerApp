package com.example.expensetrackerapp.data.repository

import com.example.expensetrackerapp.data.local.Expense
import com.example.expensetrackerapp.data.local.ExpenseDao
import javax.inject.Inject

class ExpenseRepository @Inject constructor(private val dao: ExpenseDao) {

    suspend fun addExpense(expense: Expense) = dao.insertExpense(expense)

    suspend fun getExpensesForDate(date: Long) = dao.getExpensesForDate(date)

    suspend fun getAllExpenses() = dao.getAllExpenses()

    suspend fun deleteExpense(expense: Expense) = dao.deleteExpense(expense)

    suspend fun getTotalForDate(date: Long) = dao.getTotalForDate(date)
}