package com.day.line.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.day.line.ui.components.CalendarStrip
import com.day.line.ui.components.TimelineNode
import com.day.line.ui.theme.*
import com.day.line.data.Task
import com.day.line.ui.util.TaskIconUtils
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DaylineScreen(
    viewModel: TaskViewModel
) {
    // State for task details bottom sheet
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var showEditTaskDialog by remember { mutableStateOf(false) }
    
    // Current Time State (Updated every minute)
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    LaunchedEffect(Unit) {
        while(true) {
            currentTime = LocalTime.now()
            kotlinx.coroutines.delay(30000L) // Update every 30 seconds
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Theme background
    ) {
        val selectedDate by viewModel.selectedDate.collectAsState()
        
        // Calendar Section with Grey Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else PastelGrey)
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
                .background(MaterialTheme.colorScheme.background), // Theme background for tasks
            contentPadding = PaddingValues(bottom = 100.dp) // Space for FAB/Nav + Nav Bar
        ) {
            // Spacer at top of list
            item { Spacer(modifier = Modifier.height(16.dp)) }

            items(timelineItems.size) { index ->
                val item = timelineItems[index]
                val isLast = index == timelineItems.lastIndex
                val nextItem = timelineItems.getOrNull(index + 1)

                // Shared Time Parsing Helper
                fun parseTime(timeStr: String): LocalTime {
                    return try {
                        LocalTime.parse(timeStr)
                    } catch (e: Exception) {
                        try {
                             LocalTime.parse(timeStr, java.time.format.DateTimeFormatter.ofPattern("h:mm a", java.util.Locale.US))
                        } catch (e2: Exception) {
                             LocalTime.of(0, 0) // Fallback
                        }
                    }
                }

                // Determine Next Color for Gradient
                val nextColor = nextItem?.let {
                    when (it) {
                        is TaskViewModel.TimelineItem.Fixed -> Color(it.colorHex)
                        is TaskViewModel.TimelineItem.UserTask -> getTaskColor(it.task)
                    }
                }

                // Helper to get time safely
                fun getItemTime(tItem: TaskViewModel.TimelineItem): LocalTime {
                     return when(tItem) {
                         is TaskViewModel.TimelineItem.Fixed -> parseTime(tItem.time)
                         is TaskViewModel.TimelineItem.UserTask -> parseTime(tItem.task.startTime)
                     }
                }

                // Calculate Progress
                val startTime = getItemTime(item)
                val endTime = when (item) {
                    is TaskViewModel.TimelineItem.UserTask -> parseTime(item.task.endTime)
                    // For fixed items, use next item start time as "end" of the segment (gap), or default 30m if last
                    is TaskViewModel.TimelineItem.Fixed -> {
                        nextItem?.let { getItemTime(it) } ?: startTime.plusMinutes(30)
                    }
                }

                val progress = calculateProgress(currentTime, startTime, endTime)

                when (item) {
                    is TaskViewModel.TimelineItem.Fixed -> {
                        TimelineNode(
                            time = item.time,
                            title = item.title,
                            subtitle = item.subtitle,
                            icon = TaskIconUtils.getIconByName(item.iconName),
                            color = Color(item.colorHex), // Convert Long to Color
                            isLast = isLast,
                            isCompleted = item.isCompleted,
                            nextColor = nextColor,
                            progress = progress,
                            onToggleCompletion = {
                                viewModel.toggleFixedItemCompletion(selectedDate, item.title)
                            },
                            onClick = { 
                                // Create a temporary task object for editing
                                val tempTask = Task(
                                    id = 0, // New task
                                    title = item.title,
                                    date = selectedDate,
                                    startTime = item.time,
                                    endTime = java.time.LocalTime.parse(item.time).plusMinutes(30).format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                                    isAllDay = false,
                                    notes = item.subtitle,
                                    icon = item.iconName,
                                    isCompleted = item.isCompleted
                                )
                                selectedTask = tempTask
                            }
                        )
                    }
                    is TaskViewModel.TimelineItem.UserTask -> {
                        val task = item.task
                        // Parse subtasks
                        val subtasksList = try {
                            if (task.subtasks.isNotEmpty()) {
                                val jsonArray = org.json.JSONArray(task.subtasks)
                                List(jsonArray.length()) { jsonArray.getString(it) }
                            } else emptyList()
                        } catch (e: Exception) { emptyList() }

                        TimelineNode(
                            time = task.startTime,
                            endTime = task.endTime,
                            duration = task.getDuration(),
                            durationMinutes = try {
                                java.time.Duration.between(parseTime(task.startTime), parseTime(task.endTime)).toMinutes()
                            } catch (e: Exception) { null },
                            title = task.title,
                            subtitle = null,  
                            subtasks = subtasksList,
                            notes = task.notes.takeIf { it.isNotEmpty() },
                            
                            icon = TaskIconUtils.getIconByName(task.icon),
                            color = getTaskColor(task), // Dynamic deterministic color
                            isLast = isLast,
                            isCompleted = task.isCompleted,
                            nextColor = nextColor,
                            progress = progress,
                            onToggleCompletion = {
                                viewModel.updateTask(task.copy(isCompleted = !task.isCompleted))
                            },
                            onClick = {
                                selectedTask = task
                            }
                        )
                    }
                }
            }
        }
    }

    // Task Details Bottom Sheet
    selectedTask?.let { task ->
        if (!showEditTaskDialog) {
            com.day.line.ui.components.TaskDetailsBottomSheet(
                task = task,
                color = getTaskColor(task), // Pass dynamic color
                onDismiss = { selectedTask = null },
                onDelete = {
                    viewModel.deleteTask(task)
                    selectedTask = null
                },
                onDuplicate = {
                    val duplicate = task.copy(
                        id = 0, // Reset ID for new entry
                        title = "${task.title} (Copy)"
                    )
                    viewModel.addTask(duplicate)
                    selectedTask = null
                },
                onEdit = {
                    showEditTaskDialog = true
                },
                onToggleCompletion = {
                    viewModel.updateTask(task.copy(isCompleted = !task.isCompleted))
                    // Update selected task to reflect change immediately in UI
                    selectedTask = task.copy(isCompleted = !task.isCompleted)
                }
            )
        }
    }

    // Edit Task Dialog
    selectedTask?.let { task ->
        if (showEditTaskDialog) {
            com.day.line.ui.components.AddTaskDialog(
                selectedDate = task.date,
                taskToEdit = task,
                onDismiss = { 
                    showEditTaskDialog = false 
                    selectedTask = null
                },
                onSave = { updatedTask ->
                    if (updatedTask.id == 0L) {
                        viewModel.addTask(updatedTask)
                    } else {
                        viewModel.updateTask(updatedTask)
                    }
                    showEditTaskDialog = false
                    selectedTask = null
                }
            )
        }
    }
}

fun calculateProgress(current: LocalTime, start: LocalTime, end: LocalTime): Float {
    val currentMinutes = current.hour * 60 + current.minute
    val startMinutes = start.hour * 60 + start.minute
    var endMinutes = end.hour * 60 + end.minute

    // Handle overnight tasks (end time is smaller than start time)
    if (endMinutes < startMinutes) {
        endMinutes += 24 * 60
    }
    
    var effectiveCurrent = currentMinutes
    // If we are in "next day" territory relative to start
    if (effectiveCurrent < startMinutes && effectiveCurrent + 24*60 <= endMinutes + (24*60)) { // rough heuristic
         effectiveCurrent += 24 * 60
    }
    
    if (effectiveCurrent < startMinutes) return 0f
    if (effectiveCurrent >= endMinutes) return 1f
    
    val totalDuration = endMinutes - startMinutes
    if (totalDuration == 0) return 1f // Zero duration task is instantly done
    
    return (effectiveCurrent - startMinutes).toFloat() / totalDuration
}

// getIconByName and getTaskIcon removed - moved to TaskIconUtils

private fun getTaskColor(task: Task): androidx.compose.ui.graphics.Color {
    // Use manual color if set
    task.color?.let { return Color(it) }
    
    // Fallback: Deterministic color based on task title hash
    // This ensures the same task always gets the same color, but different tasks get different colors.
    val index = kotlin.math.abs(task.title.hashCode()) % TaskPalette.size
    return TaskPalette[index]
}
