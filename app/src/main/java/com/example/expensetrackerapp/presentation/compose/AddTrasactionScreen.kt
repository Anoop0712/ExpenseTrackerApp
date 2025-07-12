package com.example.expensetrackerapp.presentation.compose

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun AddTransactionScreen(
    onSaveClick: () -> Unit = {}
) {

    // State management
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("Sunday, Jul 7, 2024") }
    var type by remember { mutableStateOf("Expense") }
    var category by remember { mutableStateOf("Food") }
    var notes by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Image Picker Box
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.2f))
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Default.Add, contentDescription = "Add Image", tint = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Text fields
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Type") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Notes section
        Text(
            "Transaction notes",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF4F4F4))
                .padding(8.dp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onSaveClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

