package com.example.expensetrackerapp.presentation.compose

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.expensetrackerapp.data.local.CategoryType
import com.example.expensetrackerapp.data.local.Expense
import com.example.expensetrackerapp.presentation.ExpenseViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun ExpenseEntryScreen(navController: NavHostController, viewModel: ExpenseViewModel) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val title = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf(CategoryType.OTHER) }
    val notes = remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf("") }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = copyUriToInternalStorage(context, uri)
    }

    LaunchedEffect(Unit) {
        viewModel.getTotalAmountSpentToday()
    }

    Scaffold(
        topBar = {
            TopBar(title = "Add Expense") {
                navController.popBackStack()
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Total Spent Today: ₹${viewModel.totalAmountSpentToday.collectAsState().value}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.2f))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri.isNotBlank()) {
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
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                OutlinedTextField(
                    value = amount.value,
                    onValueChange = { amount.value = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                ExpandItemUI(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = "Category",
                    items = CategoryType.values().map { it.name },
                    selectedValue = selectedCategory.value.name

                ) {
                    selectedCategory.value = CategoryType.valueOf(it)
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(
                    text = "Expense Notes",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                TextField(
                    value = notes.value,
                    onValueChange = { if (it.length <= 100) notes.value = it },
                    label = { Text("Notes") },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                SubmitWithAnimation(
                    "Add Expense",
                    2000,
                    title.value.isNotBlank() && amount.value.toIntOrNull() != null && amount.value.toInt() > 0,
                    operation = {
                        keyboardController?.hide()
                        val expense = Expense(
                            title = title.value,
                            amount = amount.value.toInt(),
                            category = selectedCategory.value,
                            notes = notes.value,
                            receiptUri = imageUri
                        )
                        viewModel.addExpense(expense)
                    }
                ) {
                    navController.popBackStack()
                    Toast.makeText(context, "Expense Added", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

fun copyUriToInternalStorage(context: Context, uri: Uri?): String {
    return uri?.let {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    }.orEmpty()

}

