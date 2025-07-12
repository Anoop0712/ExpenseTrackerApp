package com.example.expensetrackerapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.data.local.Expense
import com.example.expensetrackerapp.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    init {
        loadTodayExpenses()
    }

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    fun loadTodayExpenses() {
        viewModelScope.launch {
            repository.getExpensesForDate(LocalDate.now()).collect { list ->
                _uiState.value = _uiState.value.copy(
                    expenses = list,
                    totalAmount = list.sumOf { it.amount }
                )
            }

        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addExpense(expense)
            loadTodayExpenses()
        }
    }
}

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val totalAmount: Int = 0
)