package com.example.expensetrackerapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String? = null,
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
