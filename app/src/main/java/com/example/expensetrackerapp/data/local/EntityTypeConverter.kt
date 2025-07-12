package com.example.expensetrackerapp.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

class EntityTypeConverter {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString)
    }
}