package com.example.expensetrackerapp.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.expensetrackerapp.data.local.CategoryType
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun generateCSV(
    context: Context,
    dailyTotals: List<Pair<LocalDate, Int>>,
    categoryTotals: Map<CategoryType, Int>
): Uri? {
    val fileName = "expense_report_${System.currentTimeMillis()}.csv"
    val file = File(context.cacheDir, fileName)

    try {
        file.printWriter().use { out ->
            out.println("Expense Report")
            out.println("Generated on,${LocalDate.now()}")
            out.println()

            out.println("Daily Totals:")
            out.println("Date,Amount")
            dailyTotals.forEach { (date, amount) ->
                out.println("${date.format(DateTimeFormatter.ISO_DATE)},₹$amount")
            }

            out.println()
            out.println("Category Totals:")
            out.println("Category,Amount")
            categoryTotals.forEach { (category, amount) ->
                out.println("$category,₹$amount")
            }
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}