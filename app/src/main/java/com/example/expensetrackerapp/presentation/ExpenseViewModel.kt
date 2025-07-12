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

    private val _last7daysUiState = MutableStateFlow(emptyList<Expense>())
    val last7daysUiState: StateFlow<List<Expense>> = _last7daysUiState.asStateFlow()

    private val _totalAmountSpentToday = MutableStateFlow(0)
    val totalAmountSpentToday: StateFlow<Int> = _totalAmountSpentToday.asStateFlow()

    private val _expenseAdded = MutableStateFlow(ExpenseAddedType.DEFAULT)
    val expenseAdded: StateFlow<ExpenseAddedType> = _expenseAdded.asStateFlow()

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
            val result = repository.addExpenseIfNotDuplicate(expense)
            if (result) {
                _expenseAdded.value = ExpenseAddedType.ADDED
                loadExpensesForDate(_uiState.value.selectedDate)
            } else {
                _expenseAdded.value = ExpenseAddedType.FAILED
            }

        }
    }

    fun setGroupingMode(mode: GroupingMode) {
        _uiState.value = _uiState.value.copy(groupingMode = mode)
    }

    fun loadExpensesForLast7Days() {
        val today = LocalDate.now()
        val last7Days = (0..6).map { today.minusDays(it.toLong()) }

        viewModelScope.launch {
            repository.getAllExpenses().collect { allExpenses ->
                val filtered = allExpenses.filter { it.date in last7Days }
                _last7daysUiState.value = filtered
            }
        }
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

enum class ExpenseAddedType {
    ADDED,
    FAILED,
    DEFAULT;
}