package com.example.expensetrackerapp.presentation.compose

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, onBackClick: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(text = title, modifier = Modifier.wrapContentWidth(), textAlign = TextAlign.Center) },
        navigationIcon = {
            IconButton(onClick = {
                if (onBackClick != null) {
                    onBackClick()
                }
            }) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}