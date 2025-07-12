package com.example.expensetrackerapp.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.R
import com.example.expensetrackerapp.toCapitalizeWords

@Composable
fun ExpandItemUI(
    modifier: Modifier = Modifier,
    label: String,
    selectedValue: String,
    items: List<String>,
    onClickItem: (String) -> Unit
) {
    var showMoreItems by remember { mutableStateOf(false) }
    var selectedText by remember(items) { mutableStateOf(selectedValue) }

    val state = LazyListState()
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = label
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showMoreItems = !showMoreItems }
        ) {
            Text(
                text = selectedText.toCapitalizeWords(),
                modifier = Modifier.align(Alignment.TopStart)
            )
            Icon(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .align(Alignment.TopEnd),
                painter = painterResource(R.drawable.ic_chevron_down),
                contentDescription = ""
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            thickness = 1.dp
        )
        if (showMoreItems && items.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    LazyColumn(
                        state = state,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 144.dp)
                    ) {
                        itemsIndexed(items) { index, item ->
                            Text(
                                text = item.toCapitalizeWords(),
                                modifier = Modifier
                                    .padding(horizontal = 4.dp, vertical = 8.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        if (item != selectedText) {
                                            onClickItem.invoke(item)
                                        }
                                        selectedText = item
                                        showMoreItems = !showMoreItems
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}