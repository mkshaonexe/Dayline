package com.day.line.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.day.line.ui.theme.DaylineOrange
import com.day.line.ui.theme.ElectricBlue
import com.day.line.ui.theme.DaylineTheme
import com.day.line.ui.theme.SoftTeal

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp) // Generous height for spacing
    ) {
        // Time Column - Modern Typography
        Text(
            text = time,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = if (time.isNotEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else Color.Transparent,
            modifier = Modifier
                .width(80.dp)
                .padding(top = 30.dp, end = 16.dp),
            textAlign = TextAlign.End
        )

        // Timeline Line & Node
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            // The connecting line
            if (!isLast) {
                Canvas(modifier = Modifier
                    .width(4.dp) // Thicker, bolder line
                    .fillMaxHeight()
                    .padding(top = 50.dp) // Start after the node
                ) {
                    val pathEffect = if (type == TimelineNodeType.GAP) {
                        PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                    } else {
                        null
                    }

                    // Draw line
                    drawLine(
                        color = if (type == TimelineNodeType.GAP) Color.Gray.copy(alpha = 0.3f) else color.copy(alpha = 0.2f),
                        start = Offset(center.x, 0f),
                        end = Offset(center.x, size.height + 40.dp.toPx()), 
                        strokeWidth = 3.dp.toPx(),
                        pathEffect = pathEffect
                    )
                }
            }

            // The Node Icon
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(if (type == TimelineNodeType.START || type == TimelineNodeType.END) 60.dp else 44.dp)
                    .background(color = color.copy(alpha = 0.1f), shape = CircleShape) // Glow ring
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (type == TimelineNodeType.GAP) Color.Transparent else color
                    )
                    .then(
                        if (icon == null && type == TimelineNodeType.GAP) {
                            Modifier.background(Color.Gray.copy(alpha = 0.2f), CircleShape)
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                } else if (type == TimelineNodeType.TASK) {
                    // Start of a task - white inner dot
                     Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(Color.White, CircleShape)
                    )
                } else if (type == TimelineNodeType.GAP) {
                     // Gap dot
                     Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color.Gray.copy(alpha = 0.5f), CircleShape)
                    )
                }
            }
        }

        // Content
        Column(
            modifier = Modifier
                .padding(start = 24.dp, top = 26.dp, end = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
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
    // Re-use logic or style for standard tasks
    TimelineNode(
        time = time,
        title = title,
        subtitle = if (duration.isNotEmpty()) duration else null,
        type = TimelineNodeType.TASK,
        color = color,
        isLast = isLast
    )
}


@Preview(showBackground = true)
@Composable
fun TimelinePreview() {
    DaylineTheme {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            TimelineNode(
                time = "08:00 AM",
                title = "Rise and Shine",
                type = TimelineNodeType.START,
                color = DaylineOrange,
                icon = Icons.Default.WbSunny
            )
            TimelineNode(
                time = "",
                title = "Reflect on the respite",
                type = TimelineNodeType.GAP,
                color = Color.Gray,
                subtitle = "Gap time"
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
