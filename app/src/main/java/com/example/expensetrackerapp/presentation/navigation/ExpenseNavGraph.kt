package com.example.expensetrackerapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetrackerapp.presentation.ExpenseViewModel
import com.example.expensetrackerapp.presentation.compose.ExpenseEntryScreen
import com.example.expensetrackerapp.presentation.compose.ExpenseListScreen
import com.example.expensetrackerapp.presentation.compose.ExpenseReportScreen

@Composable
fun ExpenseNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: ExpenseViewModel
) {
    NavHost(navController, startDestination = "list") {
        composable("list") { ExpenseListScreen(navController, viewModel) }
        composable("entry") { ExpenseEntryScreen(navController, viewModel) }
        composable("report") { ExpenseReportScreen(navController, viewModel) }
    }
}