package com.example.expensetrackerapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetrackerapp.data.repository.ExpenseRepository
import com.example.expensetrackerapp.presentation.ExpenseViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ExpenseViewModel::class.java) -> {
                ExpenseViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
