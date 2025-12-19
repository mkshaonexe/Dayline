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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp) // Fixed height for now, dynamic later
    ) {
        // Time Column
        Text(
            text = time,
            style = MaterialTheme.typography.labelMedium,
            color = SoftGray,
            modifier = Modifier
                .width(60.dp)
                .padding(top = 24.dp, end = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
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
                    .width(2.dp)
                    .fillMaxHeight()
                    .padding(top = 40.dp) // Start after the node
                ) {
                    val pathEffect = if (type == TimelineNodeType.GAP) {
                        PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    } else {
                        null
                    }

                    drawLine(
                        color = if (type == TimelineNodeType.GAP) SoftGray else color,
                        start = Offset(center.x, 0f),
                        end = Offset(center.x, size.height),
                        strokeWidth = 4.dp.toPx(),
                        pathEffect = pathEffect
                    )
                }
            }

            // The Node Icon
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(if (type == TimelineNodeType.START || type == TimelineNodeType.END) 48.dp else 40.dp)
                    .background(color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
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
                color = WarmPink,
                icon = Icons.Default.WbSunny
            )
            TimelineNode(
                time = "",
                title = "Reflect on the respite",
                type = TimelineNodeType.GAP,
                color = SoftGray,
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
