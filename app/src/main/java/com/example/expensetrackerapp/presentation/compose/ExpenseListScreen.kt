package com.example.expensetrackerapp.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.expensetrackerapp.data.local.Expense
import com.example.expensetrackerapp.presentation.ExpenseViewModel
import com.example.expensetrackerapp.toCapitalizeWords
import java.time.LocalDate

@Composable
fun ExpenseListScreen(navController: NavHostController, viewModel: ExpenseViewModel) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    var fabExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val isToday by remember { mutableStateOf(state.selectedDate == LocalDate.now()) }

    Scaffold(
        floatingActionButton = {
            Box(modifier = Modifier.padding(16.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    AnimatedVisibility(visible = fabExpanded) {
                        FloatingActionButton(onClick = { navController.navigate("report") }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Report")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedVisibility(visible = fabExpanded) {
                        FloatingActionButton(onClick = { navController.navigate("entry") }) {
                            Icon(Icons.Default.Add, contentDescription = "Add Expense")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    FloatingActionButton(
                        onClick = { fabExpanded = !fabExpanded },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = if (fabExpanded) Icons.Default.Close else Icons.Default.Menu,
                            contentDescription = "Toggle"
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Expenses ${if (isToday) "today" else state.selectedDate.toEpochDay()}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showDatePicker = !showDatePicker }
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = state.selectedDate.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Items(state.expenses)
            if (state.expenses.isEmpty()) Text("No expenses found")
        }
        if (showDatePicker) {
            val date = state.selectedDate

            LaunchedEffect(Unit) {
                val dialog = android.app.DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selected = LocalDate.of(year, month + 1, dayOfMonth)
                        viewModel.loadExpensesForDate(selected)
                        showDatePicker = false
                    },
                    date.year,
                    date.monthValue - 1,
                    date.dayOfMonth
                )
                dialog.setOnDismissListener {
                    showDatePicker = false
                }
                dialog.show()
            }
        }

    }
}

@Composable
private fun Items(
    items: List<Expense>
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
    ) {
        items.forEachIndexed { _, item ->
            Item(
                expense = item
            )
        }
    }
}

@Composable
private fun Item(
    expense: Expense
) {
    Card(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (expense.receiptUri.isNullOrBlank().not()) {
                AsyncImage(
                    modifier = Modifier.size(height = 100.dp, width = 100.dp),
                    model = expense.receiptUri.orEmpty(),
                    contentDescription = null
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Name: ${expense.title.toCapitalizeWords()}")
                Text("Amount: ₹${expense.amount}")
                if (expense.notes.isNullOrBlank().not()) {
                    Text("Notes: ${expense.notes.toCapitalizeWords()}")
                }
            }
        }
    }
}