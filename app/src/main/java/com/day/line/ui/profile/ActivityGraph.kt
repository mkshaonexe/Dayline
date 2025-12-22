package com.day.line.ui.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
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
    barColor: Color = DaylineOrange
) {
    if (data.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()
    val labelTextStyle = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 10.sp
    )
    val maxCount = remember(data) { data.maxOfOrNull { it.count }?.coerceAtLeast(1) ?: 1 }

    Canvas(modifier = modifier.padding(16.dp)) {
        val width = size.width
        val height = size.height
        val barWidth = 24.dp.toPx()
        val spacing = (width - (data.size * barWidth)) / (data.size + 1)
        val maxBarHeight = height - 30.dp.toPx() // Leave space for labels

        data.forEachIndexed { index, item ->
            // Parse date to get short day name
            val date = try {
                LocalDate.parse(item.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (e: Exception) {
                LocalDate.now()
            }
            val dayName = date.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault()))

            val x = spacing + (index * (barWidth + spacing))
            
            // Calculate bar height relative to max
            val barHeight = (item.count.toFloat() / maxCount) * maxBarHeight
            val barTop = maxBarHeight - barHeight

            // Draw Bar
            drawRoundRect(
                color = if (item.count > 0) barColor else barColor.copy(alpha = 0.3f),
                topLeft = Offset(x, barTop + 10.dp.toPx()), // Add some top padding
                size = Size(barWidth, barHeight.coerceAtLeast(4.dp.toPx())), // Min height for visibility
                cornerRadius = CornerRadius(4.dp.toPx())
            )

            // Draw Label
            drawText(
                textMeasurer = textMeasurer,
                text = dayName,
                style = labelTextStyle,
                topLeft = Offset(
                    x + (barWidth / 2) - (textMeasurer.measure(dayName, labelTextStyle).size.width / 2),
                    height - 15.dp.toPx()
                )
            )
        }
    }
}
