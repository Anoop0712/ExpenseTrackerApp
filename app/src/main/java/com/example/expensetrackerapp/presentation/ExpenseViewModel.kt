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
        loadExpensesForDate(LocalDate.now())
    }

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    private val _totalAmountSpentToday = MutableStateFlow(0)
    val totalAmountSpentToday: StateFlow<Int> = _totalAmountSpentToday.asStateFlow()

    fun loadExpensesForDate(date: LocalDate) {
        viewModelScope.launch {
            repository.getExpensesForDate(date).collect { list ->
                _uiState.value = _uiState.value.copy(
                    selectedDate = date,
                    expenses = list,
                    totalAmount = list.sumOf { it.amount }
                )
            }
        }
    }

    fun getTotalAmountSpentToday() {
        viewModelScope.launch {
            repository.getExpensesForDate(LocalDate.now()).collect { list ->
               _totalAmountSpentToday.value = list.sumOf { it.amount }
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addExpense(expense)
            loadExpensesForDate(_uiState.value.selectedDate)
        }
    }

    fun setGroupingMode(mode: GroupingMode) {
        _uiState.value = _uiState.value.copy(groupingMode = mode)
    }
}

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val totalAmount: Int = 0,
    val selectedDate: LocalDate = LocalDate.now(),
    val groupingMode: GroupingMode = GroupingMode.CATEGORY
)

enum class GroupingMode {
    CATEGORY,
    HOUR_TIME
}