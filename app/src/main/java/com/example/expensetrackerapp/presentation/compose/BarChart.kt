package com.example.expensetrackerapp.presentation.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    data: List<Pair<String, Int>> = listOf(Pair("hi", 20), Pair("hello", 10)),
    barColor: Color = Color(0xFF6200EE),
    labelColor: Color = Color.Black,
    maxBarHeight: Dp = 200.dp
) {
    val maxAmount = data.maxOfOrNull { it.second }?.takeIf { it > 0 } ?: 1

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxBarHeight)
                .padding(vertical = 8.dp)
        ) {
            val barWidth = size.width / (data.size * 2)
            data.forEachIndexed { index, (label, amount) ->
                val left = index * 2 * barWidth + barWidth / 2
                val barHeight = (amount / maxAmount.toFloat()) * size.height
                drawRect(
                    color = barColor,
                    topLeft = Offset(left, size.height - barHeight),
                    size = Size(barWidth, barHeight)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach { (label, _) ->
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = labelColor,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
