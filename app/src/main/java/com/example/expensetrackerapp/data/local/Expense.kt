package com.example.expensetrackerapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Int,
    val category: String,
    val notes: String?,
    val date: LocalDate = LocalDate.now(),
    val receiptUri: String? = null
)
