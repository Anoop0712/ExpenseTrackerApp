package com.example.expensetrackerapp.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.expensetrackerapp.data.local.Expense
import com.example.expensetrackerapp.presentation.ExpenseViewModel
import com.example.expensetrackerapp.presentation.GroupingMode
import com.example.expensetrackerapp.util.toCapitalizeWords
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ExpenseListScreen(navController: NavHostController, viewModel: ExpenseViewModel) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    var fabExpanded by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val isToday by rememberSaveable { mutableStateOf(state.selectedDate == LocalDate.now()) }

    val grouped = when (state.groupingMode) {
        GroupingMode.CATEGORY -> state.expenses.groupBy { it.category }
        GroupingMode.HOUR_TIME -> state.expenses
            .sortedByDescending { it.time } // optional: if you want chronological
            .groupBy { it.time.hour } // or group by time component if you have time
    }

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
                .padding(top = 16.dp)
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

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Total Count: ${state.expenses.size}",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Total Amount: ${state.expenses.sumOf { it.amount }}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (state.expenses.isNotEmpty()) {
                ExpandItemUI(
                    modifier = Modifier.padding(vertical = 16.dp),
                    label = "GroupBy",
                    items = GroupingMode.values().map { it.name },
                    onClickItem = {
                        viewModel.setGroupingMode(GroupingMode.valueOf(it))
                    },
                    selectedValue = state.groupingMode.name
                )
            }
            Items(grouped, state.groupingMode)
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
    items: Map<out Any, List<Expense>>,
    groupingMode: GroupingMode,
) {
    val state = LazyListState()
    LazyColumn(
        state = state,
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        items.forEach { (group, items) ->
            item {
                Text(
                    text = when (groupingMode) {
                        GroupingMode.CATEGORY -> group.toString()
                        GroupingMode.HOUR_TIME -> {
                            if (group is LocalDateTime) group.toLocalTime().toString()
                            else group.toString()
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            items(items) { expense ->
                Item(expense)
            }
        }
    }

}

@Composable
private fun Item(
    expense: Expense
) {
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(8.dp),
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