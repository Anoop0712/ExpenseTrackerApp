package com.example.expensetrackerapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Int,
    val category: CategoryType,
    val notes: String?,
    val date: LocalDate = LocalDate.now(),
    val time: LocalDateTime = LocalDateTime.now(),
    val receiptUri: String? = null
)

enum class CategoryType {
    FOOD,
    GAMES,
    MOVIES,
    SPORTS,
    TRAVEL,
    OTHER;
}
