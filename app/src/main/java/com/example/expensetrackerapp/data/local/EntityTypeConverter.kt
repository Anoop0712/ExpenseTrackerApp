package com.example.expensetrackerapp.data.local

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EntityTypeConverter {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString)
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDateTime(dateString: String): LocalDateTime {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun fromCategory(searchType: CategoryType): String {
        return searchType.name
    }

    @TypeConverter
    fun toCategory(searchType: String): CategoryType {
        return CategoryType.valueOf(searchType)
    }
}