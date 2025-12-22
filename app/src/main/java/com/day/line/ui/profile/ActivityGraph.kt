package com.day.line.ui.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.day.line.data.DailyTaskCount
import com.day.line.ui.theme.DaylineOrange
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ActivityGraph(
    data: List<DailyTaskCount>,
    modifier: Modifier = Modifier,
    lineColor: Color = DaylineOrange,
    axisColor: Color = Color.Gray
) {
    if (data.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()
    val labelTextStyle = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 10.sp
    )
    val maxCount = remember(data) { 
        (data.maxOfOrNull { it.count }?.coerceAtLeast(4) ?: 4).toFloat() 
    }

    val surfaceColor = MaterialTheme.colorScheme.surface

    Canvas(modifier = modifier.padding(start = 32.dp, end = 16.dp, bottom = 24.dp, top = 16.dp)) {
        val width = size.width
        val height = size.height
        
        // --- Y-Axis Labels (0, Max/2, Max) ---
        val yLabels = listOf(0, (maxCount / 2).toInt(), maxCount.toInt())
        yLabels.forEach { labelValue ->
            val normalizedY = 1f - (labelValue / maxCount)
            val yPos = normalizedY * height
            
            drawText(
                textMeasurer = textMeasurer,
                text = labelValue.toString(),
                style = labelTextStyle,
                topLeft = Offset(x = -24.dp.toPx(), y = yPos - 6.sp.toPx()) // Align to left of graph
            )
        }

        // Draw Axes Lines
        drawLine(
            color = axisColor.copy(alpha = 0.5f),
            start = Offset(0f, 0f),
            end = Offset(0f, height),
            strokeWidth = 2f
        )
        drawLine(
            color = axisColor.copy(alpha = 0.5f),
            start = Offset(0f, height),
            end = Offset(width, height),
            strokeWidth = 2f
        )

        // --- Plot Points & Line ---
        if (data.size < 2) return@Canvas 

        val spacing = width / (data.size - 1)
        val path = Path()
        val points = mutableListOf<Offset>()

        data.forEachIndexed { index, item ->
            // X Calculate
            val x = index * spacing
            
            // Y Calculate
            val normalizedCount = item.count / maxCount
            val y = height - (normalizedCount * height)
            
            points.add(Offset(x, y))

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            // X-Axis Label (Day Name)
            val date = try {
                LocalDate.parse(item.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (e: Exception) {
                LocalDate.now()
            }
            val dayName = date.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault()))

            drawText(
                textMeasurer = textMeasurer,
                text = dayName,
                style = labelTextStyle,
                topLeft = Offset(
                    x = x - (textMeasurer.measure(dayName, labelTextStyle).size.width / 2),
                    y = height + 8.dp.toPx()
                )
            )
        }

        // Draw Path (Line)
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )

        // Draw Points (Dots)
        points.forEach { point ->
            drawCircle(
                color = surfaceColor, // Inner hole
                radius = 5.dp.toPx(),
                center = point
            )
            drawCircle(
                color = lineColor, // Outer ring
                radius = 4.dp.toPx(),
                center = point
            )
        }
    }
}
