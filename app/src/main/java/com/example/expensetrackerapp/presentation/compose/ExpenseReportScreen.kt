package com.example.expensetrackerapp.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expensetrackerapp.presentation.ExpenseViewModel

@Composable
fun ExpenseReportScreen(navController: NavHostController, viewModel: ExpenseViewModel) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopBar(title = "Reports") {
                navController.popBackStack()
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Expense Report (Last 7 Days)", style = MaterialTheme.typography.titleLarge)
            Text("Total Spent: ₹${state.totalAmount}")
            Text("[Mock Chart Here]")
        }
    }
}