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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.day.line.ui.theme.DaylineOrange

@Composable
fun ActivityGraph(
    data: List<GraphPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = DaylineOrange,
    pointColor: Color = DaylineOrange,
    axisColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    if (data.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()
    val labelTextStyle = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 11.sp
    )
    
    // Calculate max value with some padding at top
    val maxCount = remember(data) { 
        val max = data.maxOfOrNull { it.value } ?: 0
        if (max == 0) 4f else (max * 1.2f).coerceAtLeast(4f)
    }
    
    val surfaceColor = MaterialTheme.colorScheme.surface
    
    Canvas(modifier = modifier.padding(start = 40.dp, end = 16.dp, bottom = 24.dp, top = 16.dp)) {
        val width = size.width
        val height = size.height
        
        // --- Y-Axis Labels & Grid Lines ---
        val steps = 4
        val stepValue = maxCount / steps
        
        for (i in 0..steps) {
            val value = (stepValue * i).toInt()
            val yRatio = i.toFloat() / steps
            val yPos = height - (yRatio * height)
            
            // Grid line
            drawLine(
                color = axisColor.copy(alpha = 0.1f),
                start = Offset(0f, yPos),
                end = Offset(width, yPos),
                strokeWidth = 1.dp.toPx()
            )
            
            // Label
            val textLayoutResult = textMeasurer.measure(
                text = value.toString(),
                style = labelTextStyle
            )
            
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = -textLayoutResult.size.width.toFloat() - 8.dp.toPx(),
                    y = yPos - (textLayoutResult.size.height / 2)
                )
            )
        }

        // --- X-Axis & Data ---
        if (data.size < 2) return@Canvas 

        val spacing = width / (data.size - 1)
        val path = Path()
        val points = mutableListOf<Offset>()

        data.forEachIndexed { index, item ->
            // X Calculate
            val x = index * spacing
            
            // Y Calculate
            val ratio = item.value / maxCount
            val y = height - (ratio * height)
            
            points.add(Offset(x, y))

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            // X-Axis Label
            val dayName = item.label
            val textLayoutResult = textMeasurer.measure(dayName, labelTextStyle)
            
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = x - (textLayoutResult.size.width / 2),
                    y = height + 8.dp.toPx()
                )
            )
        }

        // Draw Line
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw Solid Points
        points.forEach { point ->
            // White background for point to clear line
            drawCircle(
                color = surfaceColor,
                radius = 6.dp.toPx(),
                center = point
            )
            // Example "Ring" style or "Solid"? Let's do solid for clarity
            drawCircle(
                color = pointColor,
                radius = 4.dp.toPx(),
                center = point
            )
        }
    }
}
