package com.day.line.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.day.line.ui.theme.*

@Composable
fun CalendarStrip() {
    val days = listOf(
        "Mon" to "12", "Tue" to "13", "Wed" to "14", "Thu" to "15",
        "Fri" to "16", "Sat" to "17", "Sun" to "18"
    )
    val selectedDay = "15"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "February 2024",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = TextDark
            )
            Row {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Calendar",
                    tint = PastelRed,
                    modifier = Modifier.size(24.dp).padding(end = 8.dp)
                )
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = PastelRed,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { (day, date) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = day,
                        color = if (date == selectedDay) PastelRed else TextLight,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (date == selectedDay) PastelRed else Color.Transparent)
                    ) {
                        Text(
                            text = date,
                            color = if (date == selectedDay) Color.White else TextDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    // Small dots for tasks
                    Row {
                         Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(PastelRed))
                         Spacer(modifier = Modifier.width(2.dp))
                         Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(PastelBlue))
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineNode(
    time: String,
    endTime: String? = null,
    duration: String? = null,
    durationMinutes: Long? = null,
    title: String,
    subtitle: String? = null,
    subtasks: List<String> = emptyList(),
    notes: String? = null,
    icon: ImageVector,
    color: Color,
    isLast: Boolean = false,
    isCompleted: Boolean = false,

    onToggleCompletion: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    // Base height for a standard node (icon + padding)
    // Base height for a standard node (icon + padding)
    val baseHeight = 80.dp 
    // Calculate dynamic height: base + (minutes * scale)
    // Scale: 1.5dp per minute seems reasonable. 60m = 90dp extra.
    val dynamicHeight = if (durationMinutes != null) {
        val minutesHeight = durationMinutes * 1.5
        (baseHeight.value + minutesHeight).dp
    } else {
        baseHeight
    }
    
    // Calculate node (pill) height based on duration
    // Keep it circular (56dp) for short tasks (<= 15 min), stretch for longer ones
    val baseNodeHeight = 56.dp
    val nodeHeight = if (durationMinutes != null && durationMinutes > 15) {
        val extraMinutes = durationMinutes - 15
        // Scale the node slightly less than the row so we still have connector lines
        (baseNodeHeight.value + (extraMinutes * 1.2)).dp
    } else {
        baseNodeHeight
    }

    IntrinsicHeightRow(
        modifier = Modifier
            .heightIn(min = dynamicHeight)
            .clickable(onClick = onClick)
    ) {
        // Time Column
        Column(
            modifier = Modifier
                .width(60.dp)
                .padding(top = 16.dp, end = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall.copy(color = TextLight, fontSize = 12.sp)
            )
        }

        // Line and Node Column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(48.dp)
                .fillMaxHeight()
        ) {
            // Node Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(56.dp)
                    .height(nodeHeight) // Dynamic height
                    .padding(vertical = 4.dp)
            ) {
                // Liquid Glass Effect
                
                // 1. Base Gradient (The "Liquid")
                // Mix the node color with a bit of gradient to give it depth
                val displayColor = if (isCompleted) Color(0xFF4CAF50) else color // Success Green or Token Color

                val baseGradient = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(
                        displayColor,
                        displayColor.copy(alpha = 0.8f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 100f)
                )

                // 2. Top Highlight (Light reflection from top)
                val topHighlight = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.45f), // Stronger highlight
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = 50f
                )

                // 3. Bottom Shadow (Inner depth)
                val bottomShadow = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.15f) // Subtle shadow
                    ),
                    startY = 50f, // starts lower
                    endY = 100f
                )

                // 4. Glass Border (The "Container" Rim)
                val glassBorder = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.7f),
                        Color.White.copy(alpha = 0.2f),
                        Color.White.copy(alpha = 0.5f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 100f)
                )

                // Apply all layers to the box
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        // Outer Shadow for 3D elevation
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = displayColor.copy(alpha = 0.5f), 
                            ambientColor = displayColor.copy(alpha = 0.2f)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(brush = baseGradient)
                        // Add border
                        .border(
                            width = 1.5.dp,
                            brush = glassBorder,
                            shape = RoundedCornerShape(20.dp)
                        )
                        // Draw highlights/shadows
                        .drawWithContent {
                            drawContent()
                            drawRect(brush = topHighlight)
                            drawRect(brush = bottomShadow)
                        }
                )
                
                // Icon - Tinted white or proper contrast color for glass look
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.75f), 
                    modifier = Modifier.size(24.dp)
                )
            }

            // Vertical Dashed Line
            if (!isLast) {
                Canvas(
                    modifier = Modifier
                        .weight(1f) // Fill remaining height
                        .width(2.dp)
                ) {
                    drawLine(
                        color = TimelineLineColor,
                        start = Offset(center.x, 0f),
                        end = Offset(center.x, size.height),
                        strokeWidth = 6f, // Increased from 4f for better visibility
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }
            } else {
                 Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Content Column
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp, start = 12.dp, bottom = 32.dp)
        ) {
            // Duration/Time range if present
            if (duration != null || endTime != null) {
                val durationText = buildString {
                    if (endTime != null) append("$time - $endTime")
                    if (endTime != null && duration != null) append(" ")
                    if (duration != null) append("($duration)")
                }
                Text(
                    text = durationText,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextLight, fontSize = 12.sp)
                )
            } else {
                 // Spacing to align with icon if no top text
                 Spacer(modifier = Modifier.height(0.dp)) 
                 // Actually the design aligns "Wake up" with the icon center roughly, or slightly below top. 
                 // Let's add top padding to title if no duration.
                 if (duration == null) Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isCompleted) TextLight else TextDark, // Grey out if completed
                    textDecoration = if (isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null // Strike through
                )
            )
            
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                     // Zzz icon or similar if needed, text for now
                     Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextLight)
                    )
                }
            }

            // Notes
            if (!notes.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextLight,
                    maxLines = 2
                )
            }

            // Subtasks
            if (subtasks.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                subtasks.forEach { subtask ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically, 
                        modifier = Modifier.padding(bottom = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(color.copy(alpha = 0.5f), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = subtask,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextDark.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
        
        // Checkbox Column
        Column(
            modifier = Modifier
                .padding(top = 24.dp, end = 16.dp),
             horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp) // Increased from 24dp for better visibility
                    .clip(CircleShape)
                    .background(if (isCompleted) DaylineOrange else Color.White)
                    .border(
                        width = 2.5.dp, // Thicker border for better visibility
                        color = if (isCompleted) DaylineOrange else Color.Gray.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .clickable { onToggleCompletion() },
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun IntrinsicHeightRow(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        content = content
    )
}
