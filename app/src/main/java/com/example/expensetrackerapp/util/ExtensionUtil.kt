package com.example.expensetrackerapp.util

import java.util.Locale

fun String?.toCapitalizeWords(): String {
    return this.orEmpty().split(" ").joinToString(" ") { it.toCapitalize() }
}

fun String?.toCapitalize(): String {
    return this.orEmpty()
        .lowercase(Locale.getDefault())
        .replaceFirstChar { char ->
            when {
                char.isLowerCase() -> char.titlecase(Locale.getDefault())
                else -> char.toString()
            }
        }
}