package com.day.line.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.day.line.data.Task
import com.day.line.ui.theme.DaylineOrange
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    selectedDate: String,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var isAllDay by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }
    
    // Parse the selected date
    val parsedDate = try {
        LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    } catch (e: Exception) {
        LocalDate.now()
    }
    
    var selectedYear by remember { mutableStateOf(parsedDate.year) }
    var selectedMonth by remember { mutableStateOf(parsedDate.monthValue) }
    var selectedDay by remember { mutableStateOf(parsedDate.dayOfMonth) }
    
    // Time values
    val now = LocalTime.now()
    // Round to nearest 15 mins for cleaner defaults
    val minute = now.minute
    val roundedMinute = ((minute + 14) / 15) * 15
    val baseTime = now.withMinute(0).plusMinutes(roundedMinute.toLong())
    
    var startTime by remember { mutableStateOf(baseTime) }
    var endTime by remember { mutableStateOf(baseTime.plusMinutes(15)) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showEnhancedTimePicker by remember { mutableStateOf(false) }
    
    val dateDisplay = LocalDate.of(selectedYear, selectedMonth, selectedDay)
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd. MMM")
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Main Overlay Background (Dimmed)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(onClick = onDismiss)
            )

            // Dialog Content
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .heightIn(max = 600.dp) // Limit maximum height
                    .wrapContentHeight() // Allow it to shrink
                    .clickable(enabled = false) {}, // Prevent clicks from closing dialog
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 1. Compact Header Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp) // Reduced height
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        DaylineOrange,     // Vibrant Orange
                                        Color(0xFFFF8533)  // Neon Orange
                                    )
                                )
                            )
                    ) {
                        // Close Button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                .size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Compact Content
                        Column(
                            modifier = Modifier
                                .padding(start = 24.dp, end = 24.dp)
                                .align(Alignment.CenterStart)
                        ) {
                            // Title Input
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (taskTitle.isEmpty()) {
                                    Text(
                                        text = "Structure Your Day", // Placeholder
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White.copy(alpha = 0.6f)
                                    )
                                }
                                BasicTextField(
                                    value = taskTitle,
                                    onValueChange = { taskTitle = it },
                                    textStyle = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Subtitle / Time Summary
                            val durationMins = ChronoUnit.MINUTES.between(startTime, endTime)
                            val timeText = if (isAllDay) "All-Day" else "${startTime.format(timeFormatter)} – ${endTime.format(timeFormatter)} ($durationMins min)"
                            
                            Text(
                                text = timeText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    // 2. Form Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        // Date Row
                        OutlinedCard(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp), // Unified shape instead of split top/bottom
                            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha=0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = DaylineOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "${dateDisplay.format(dateFormatter)} ${dateDisplay.year}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                  Checkbox(
                                      checked = isAllDay,
                                      onCheckedChange = { isAllDay = it },
                                      colors = CheckboxDefaults.colors(
                                          checkedColor = DaylineOrange,
                                          uncheckedColor = MaterialTheme.colorScheme.outline
                                      ),
                                      modifier = Modifier.scale(0.8f) // Make checkbox smaller
                                  )
                                  Text(text = "All-Day", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        // Time Row (Only if not All-Day)
                        if (!isAllDay) {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedCard(
                                onClick = { showEnhancedTimePicker = !showEnhancedTimePicker },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha=0.5f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = DaylineOrange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "${startTime.format(timeFormatter)} - ${endTime.format(timeFormatter)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            
                            // Enhanced Time Picker Dialog Logic
                            if (showEnhancedTimePicker) {
                                Dialog(
                                    onDismissRequest = { showEnhancedTimePicker = false },
                                    properties = DialogProperties(
                                        usePlatformDefaultWidth = false,
                                        decorFitsSystemWindows = false
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.5f))
                                            .clickable { showEnhancedTimePicker = false },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.9f)
                                                .clickable(enabled = false) {}
                                        ) {
                                            EnhancedTimePicker(
                                                startTime = startTime,
                                                endTime = endTime,
                                                onStartTimeChange = { newStart ->
                                                    val duration = ChronoUnit.MINUTES.between(startTime, endTime)
                                                    startTime = newStart
                                                    endTime = newStart.plusMinutes(duration)
                                                },
                                                onEndTimeChange = { newEnd ->
                                                    endTime = newEnd
                                                },
                                                onDismiss = { showEnhancedTimePicker = false }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Repeat Row
                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha=0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Repeat,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Repeat",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "PRO",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Subtask & Notes Section
                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha=0.5f))
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp))
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Add Subtask",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                // Notes Input
                                OutlinedTextField(
                                    value = notes,
                                    onValueChange = { notes = it },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    placeholder = { 
                                        Text(
                                            "Add notes, links...",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.bodyMedium
                                        ) 
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent
                                    ),
                                    minLines = 2,
                                    maxLines = 4
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Action Button
                        Button(
                            onClick = {
                                // Create and Save Task
                                val finalTitle = if (taskTitle.isBlank()) "Structure Your Day" else taskTitle
                                val task = Task(
                                    title = finalTitle,
                                    date = Task.formatDate(selectedYear, selectedMonth, selectedDay),
                                    startTime = Task.formatTime(startTime.hour, startTime.minute),
                                    endTime = Task.formatTime(endTime.hour, endTime.minute),
                                    isAllDay = isAllDay,
                                    notes = notes
                                )
                                onSave(task)
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DaylineOrange,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(
                                text = "Create Task",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = LocalDate.of(selectedYear, selectedMonth, selectedDay)
                .atStartOfDay(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {}, // Custom handling or auto-dismiss
            dismissButton = {}
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
            // Confirm button for date picker
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                            selectedYear = date.year
                            selectedMonth = date.monthValue
                            selectedDay = date.dayOfMonth
                        }
                        showDatePicker = false
                    }
                ) { Text("OK", color = DaylineOrange) }
            }
        }
    }
}
