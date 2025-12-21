package com.day.line.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.day.line.ui.components.CalendarStrip
import com.day.line.ui.components.TimelineNode
import com.day.line.ui.theme.*

@Composable
fun DaylineScreen(
    viewModel: TaskViewModel // Kept for structure, unused for static UI
) {
    // Note: Scaffold was removed here to let HomeScreen handle the main Scaffold/Navigation.
    // Based on the reference code, this screen just provides the content.
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Default background
    ) {
        val selectedDate by viewModel.selectedDate.collectAsState()
        
        // Calendar Section with Grey Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PastelGrey)
        ) {
            // Apply status bar padding to the content inside the grey box
            // so the grey background extends behind the status bar
            Box(modifier = Modifier.statusBarsPadding()) {
                CalendarStrip(
                    selectedDate = selectedDate,
                    onDateSelected = { viewModel.selectDate(it) }
                )
            }
        }

        // Visual Section Separator
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        }
        
        val timelineItems by viewModel.timelineItems.collectAsState()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White), // Explicit white background for tasks
            contentPadding = PaddingValues(bottom = 100.dp) // Space for FAB/Nav + Nav Bar
        ) {
            // Spacer at top of list
            item { Spacer(modifier = Modifier.height(16.dp)) }

            items(timelineItems.size) { index ->
                val item = timelineItems[index]
                val isLast = index == timelineItems.lastIndex


                when (item) {
                    is TaskViewModel.TimelineItem.Fixed -> {
                        TimelineNode(
                            time = item.time,
                            title = item.title,
                            subtitle = item.subtitle,
                            icon = getIconByName(item.iconName),
                            color = Color(item.colorHex), // Convert Long to Color
                            isLast = isLast,
                            isCompleted = item.isCompleted,
                            onToggleCompletion = {
                                viewModel.toggleFixedItemCompletion(selectedDate, item.title)
                            }
                        )
                    }
                    is TaskViewModel.TimelineItem.UserTask -> {
                        val task = item.task
                        TimelineNode(
                            time = task.startTime,
                            endTime = task.endTime,
                            duration = task.getDuration(),
                            durationMinutes = try {
                                val parseTime = { timeStr: String ->
                                    try {
                                        java.time.LocalTime.parse(timeStr)
                                    } catch (e: Exception) {
                                        java.time.LocalTime.parse(timeStr, java.time.format.DateTimeFormatter.ofPattern("h:mm a", java.util.Locale.US))
                                    }
                                }
                                val start = parseTime(task.startTime)
                                val end = parseTime(task.endTime)
                                java.time.Duration.between(start, end).toMinutes()
                            } catch (e: Exception) { null },
                            title = task.title,
                            subtitle = task.notes.takeIf { it.isNotEmpty() },
                            icon = getIconByName(task.icon),
                            color = DaylineOrange, // Or dynamic color
                            isLast = isLast,
                            isCompleted = task.isCompleted,
                            onToggleCompletion = {
                                viewModel.updateTask(task.copy(isCompleted = !task.isCompleted))
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun getIconByName(name: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (name) {
        "Notifications" -> Icons.Default.Notifications
        "List" -> Icons.Default.List
        "Face" -> Icons.Default.Face
        "PlayArrow" -> Icons.Default.PlayArrow
        "Bedtime" -> Icons.Default.Bedtime // Ensure this icon exists or use fallback
        "Work" -> Icons.Default.Work
        "FitnessCenter" -> Icons.Default.FitnessCenter
        "LocalCafe" -> Icons.Default.LocalCafe
        "Create" -> Icons.Default.Create
        "Code" -> Icons.Default.Code
        "MusicNote" -> Icons.Default.MusicNote
        "Book" -> Icons.Default.Book
        else -> Icons.Default.Star // Default fallback
    }
}
