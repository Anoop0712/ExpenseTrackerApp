package com.example.expensetrackerapp.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SubmitWithAnimation(buttonText: String, delay: Long, isEnable: Boolean, operation: () -> Unit) {
    var showMessage by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            operation.invoke()
            showMessage = true
        }, enabled = isEnable) {
            Text(buttonText)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Animated Visibility for the confirmation message
        AnimatedVisibility(
            visible = showMessage,
            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(),
            exit = fadeOut(animationSpec = tween(500)) + slideOutVertically()
        ) {
            Text(
                text = "Entry is being added",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Green,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Auto-hide the message after delay
        if (showMessage) {
            LaunchedEffect(Unit) {
                delay(delay) // 2 seconds
                showMessage = false
            }
        }
    }
}
