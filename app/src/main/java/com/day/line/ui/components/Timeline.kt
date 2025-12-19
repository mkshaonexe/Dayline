package com.day.line.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.day.line.ui.theme.DarkGray
import com.day.line.ui.theme.DaylineOrange
import com.day.line.ui.theme.DaylineTheme
import com.day.line.ui.theme.SoftGray
import com.day.line.ui.theme.SoftTeal
import com.day.line.ui.theme.WarmPink

enum class TimelineNodeType {
    START, END, TASK, GAP
}

@Composable
fun TimelineNode(
    time: String,
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    color: Color,
    type: TimelineNodeType,
    isLast: Boolean = false
) {
    TimelineItemLayout(
        time = time,
        title = title,
        subtitle = subtitle,
        icon = icon,
        color = color,
        isLast = isLast,
        isGap = type == TimelineNodeType.GAP
    )
}

@Composable
fun TaskTimelineNode(
    time: String,
    title: String,
    duration: String = "",
    notes: String = "",
    color: Color = DaylineOrange,
    isLast: Boolean = false,
    onClick: () -> Unit = {}
) {
    TimelineItemLayout(
        time = time,
        title = title,
        subtitle = if (duration.isNotEmpty()) duration else null, // Use duration as subtitle
        icon = Icons.Default.Event, // Default icon for tasks
        color = color,
        isLast = isLast,
        onClick = onClick
    )
}

@Composable
private fun TimelineItemLayout(
    time: String,
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    color: Color,
    isLast: Boolean = false,
    isGap: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isGap) 60.dp else 110.dp) // Adjusted height
            .clickable(enabled = !isGap) { onClick() }
    ) {
        // Time Column
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = if (time.isNotEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else Color.Transparent,
            modifier = Modifier
                .width(70.dp)
                .padding(top = 24.dp, end = 12.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )

        // Timeline Line & Node
        Box(
            modifier = Modifier
                .width(48.dp) // Slightly wider for larger icons
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            // The connecting line
            if (!isLast) {
                Canvas(modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .padding(top = 48.dp) // Start after the node center
                ) {
                    val pathEffect = if (isGap) {
                        PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    } else {
                        PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f) // Always dashed for premium look or solid? ref shows dashed/dotted often. Let's go solid for flow, dashed for gap.
                        // Actually reference 2 uses dashed line for everything. Let's try solid for main flow, dashed for gap to keep it clean.
                        // Wait, user asked to "make it professional".
                        // Let's stick to Solid for main flow, Dashed for gap.
                        null
                    }

                    drawLine(
                        color = color.copy(alpha = 0.5f),
                        start = Offset(center.x, 0f),
                        end = Offset(center.x, size.height + 40.dp.toPx()), 
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = pathEffect
                    )
                }
            }

            // The Node Icon
            if (!isGap) {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .size(48.dp)
                        .background(color, CircleShape)
                        .padding(2.dp) // Border thickness
                        .clip(CircleShape)
                        .background(Color.White) // Inner white background
                        ,
                    contentAlignment = Alignment.Center
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color, CircleShape)
                        )
                    }
                }
            } else {
                // Gap Gap icon or small dot
                 Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(8.dp)
                        .background(SoftGray, CircleShape)
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimelinePreview() {
    DaylineTheme {
        Column {
            TimelineNode(
                time = "08:00 AM",
                title = "Rise and Shine",
                type = TimelineNodeType.START,
                color = DaylineOrange, // Using DaylineOrange
                icon = Icons.Default.WbSunny
            )
            TimelineNode(
                time = "",
                title = "Gap Time",
                type = TimelineNodeType.GAP,
                color = SoftGray,
                subtitle = "Relax"
            )
            TaskTimelineNode(
                time = "10:00 AM",
                title = "Deep Work",
                duration = "2h",
                color = DaylineOrange
            )
             TimelineNode(
                time = "10:00 PM",
                title = "Wind Down",
                type = TimelineNodeType.END,
                color = SoftTeal,
                icon = Icons.Default.Bedtime,
                isLast = true
            )
        }
    }
}
