package com.day.line.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
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
                            icon = TaskIconUtils.getIconByName(item.iconName),
                            color = Color(item.colorHex), // Convert Long to Color
                            isLast = isLast,
                            isCompleted = item.isCompleted,
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
                            subtitle = null, // Subtitle is now used for duration if needed, but here we pass duration separately if TimelineNode supports it, or we rely on TimelineNode logic. Wait, TimelineNode signature changed.
                            // I need to match the NEW signature of TimelineNode I just wrote?
                            // No, I called TimelineNode directly in the replaced code above but the code I'm replacing here calls "TimelineNode" (the generic one) or "TaskTimelineNode"?
                            // The original code called TimelineNode. 
                            // My previous step updated TimelineNode signature to accept subtasks/notes.
                            // So I should pass them here.
                            
                            subtasks = subtasksList,
                            notes = task.notes.takeIf { it.isNotEmpty() },
                            
                            icon = TaskIconUtils.getIconByName(task.icon),
                            color = getTaskColor(task), // Dynamic deterministic color
                            isLast = isLast,
                            isCompleted = task.isCompleted,
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

// getIconByName and getTaskIcon removed - moved to TaskIconUtils

private fun getTaskColor(task: Task): androidx.compose.ui.graphics.Color {
    // Deterministic color based on task title hash
    // This ensures the same task always gets the same color, but different tasks get different colors.
    val index = kotlin.math.abs(task.title.hashCode()) % TaskPalette.size
    return TaskPalette[index]
}
