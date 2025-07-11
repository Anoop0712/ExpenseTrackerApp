package com.example.expensetrackerapp.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.data.local.Expense
import com.example.expensetrackerapp.presentation.ExpenseViewModel

@Composable
fun ExpenseEntryScreen(viewModel: ExpenseViewModel) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val category by remember { mutableStateOf("Food") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        TextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") })
        // Category Dropdown + Notes + Image
        Button(onClick = {
            viewModel.addExpense(
                Expense(
                    title = title,
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    category = category,
                    notes = null,
                    timestamp = System.currentTimeMillis()
                )
            )
        }) {
            Text("Submit")
        }
    }
}
