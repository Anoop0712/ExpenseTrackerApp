package com.example.expensetrackerapp.presentation.compose

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expensetrackerapp.presentation.ExpenseViewModel
import com.example.expensetrackerapp.util.generateCSV
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseReportScreen(navController: NavHostController, viewModel: ExpenseViewModel) {
    val last7daysUiState by viewModel.last7daysUiState.collectAsState()
    val context = LocalContext.current

    val dailyTotals = remember(last7daysUiState) {
        last7daysUiState.groupBy { it.date }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toSortedMap(compareByDescending { it })
            .toList()
            .take(7)
            .reversed()
    }

    val categoryTotals = remember(last7daysUiState) {
        last7daysUiState.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    val dailyBarData = dailyTotals.map { (date, amount) ->
        date.format(DateTimeFormatter.ofPattern("dd/MM")) to amount
    }

    val categoryBarData = categoryTotals.map { (category, amount) ->
        category.name to amount
    }


    LaunchedEffect(Unit) {
        viewModel.loadExpensesForLast7Days()
    }

    Scaffold(
        topBar = {
            TopBar(title = "Reports") {
                navController.popBackStack()
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(Modifier.height(16.dp))

            if (last7daysUiState.isEmpty()) {
                Text(
                    text = "No Reports to Show",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {

                Button(
                    onClick = {
                        /* Simulate CSV export */
                        val uri = generateCSV(context, dailyTotals, categoryTotals)
                        uri?.let {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/csv"
                                putExtra(Intent.EXTRA_STREAM, it)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Expense Report"))
                        }
                    }
                ) {
                    Text("Export CSV")
                }

                Text("Daily Totals", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                dailyTotals.forEach { (date, amount) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(date.format(DateTimeFormatter.ofPattern("EEE, MMM d")))
                        Text("₹$amount", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(4.dp))
                }
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    BarChart(
                        data = dailyBarData
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text("Category Totals", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                categoryTotals.forEach { (category, amount) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(category.name)
                        Text("₹$amount", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(4.dp))
                }

                Spacer(Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    BarChart(
                        data = categoryBarData
                    )
                }
            }
        }
    }
}