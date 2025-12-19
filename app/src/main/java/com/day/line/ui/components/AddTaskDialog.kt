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
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd. MMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

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
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.9f)
                    .clickable(enabled = false) {}, // Prevent clicks from closing dialog
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 1. Header Section with Gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFFD4817C), // Muted Pink/Red
                                            Color(0xFFC7716C)
                                        )
                                    )
                                )
                        ) {
                            // Close Button
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 16.dp, end = 16.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                    .size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Content inside Header
                            Column(
                                modifier = Modifier
                                    .padding(start = 24.dp, top = 60.dp)
                            ) {
                                // Timeline Graphic Placeholder
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                     // Simple visual representation of task/check
                                     Icon(
                                         imageVector = Icons.Default.Schedule, // Using schedule as placeholder
                                         contentDescription = null,
                                         tint = Color(0xFFD4817C)
                                     )
                                }
                            }
                            
                            Column(
                                modifier = Modifier
                                    .padding(start = 88.dp, top = 70.dp, end = 24.dp)
                            ) {
                                // Time Range Text
                                val durationMins = ChronoUnit.MINUTES.between(startTime, endTime)
                                Text(
                                    text = if (isAllDay) "All-Day" else "${startTime.format(DateTimeFormatter.ofPattern("h:mm a"))} – ${endTime.format(DateTimeFormatter.ofPattern("h:mm a"))} ($durationMins min)",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
BasicTextField(
                                    value = taskTitle,
                                    onValueChange = { taskTitle = it },
                                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                                    decorationBox = { innerTextField ->
                                        Box(contentAlignment = Alignment.CenterStart) {
                                            if (taskTitle.isEmpty()) {
                                                Text(
                                                    text = "Structure Your Day",
                                                    style = MaterialTheme.typography.headlineMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White.copy(alpha = 0.5f)
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(
                                    color = Color.White.copy(alpha = 0.3f),
                                    thickness = 1.dp,
                                    modifier = Modifier.width(200.dp)
                                )
                            }
                            
                            // Hollow Circle decoration
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 24.dp)
                                    .size(24.dp)
                                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                            )
                        }

                        // 2. Form Content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(24.dp)
                        ) {
                            // Date Row
                            OutlinedCard(
                                onClick = { showDatePicker = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                                colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = Color(0xFFD4817C),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = dateDisplay.format(dateFormatter),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Checkbox(
                                        checked = isAllDay,
                                        onCheckedChange = { isAllDay = it },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color(0xFFD4817C)
                                        )
                                    )
                                    Text(text = "All-Day", style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                            // Time Row (Only if not All-Day)
                            if (!isAllDay) {
                                OutlinedCard(
                                    onClick = { showEnhancedTimePicker = !showEnhancedTimePicker },
                                    modifier = Modifier.fillMaxWidth().offset(y = (-1).dp), // Overlap border
                                    shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd = 12.dp),
                                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = Color(0xFFD4817C),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "${startTime.format(timeFormatter)} - ${endTime.format(timeFormatter)}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                                
// Enhanced Time Picker Dialog
                                if (showEnhancedTimePicker) {
                                    Dialog(
                                        onDismissRequest = { showEnhancedTimePicker = false },
                                        properties = DialogProperties(
                                            usePlatformDefaultWidth = false, // Allow custom width
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
                                            // The actual picker content
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(0.9f) // Allow some side padding
                                                    .clickable(enabled = false) {} // Prevent click-through
                                            ) {
                                                EnhancedTimePicker(
                                                    startTime = startTime,
                                                    endTime = endTime,
                                                    onStartTimeChange = { newStart ->
                                                        val duration = ChronoUnit.MINUTES.between(startTime, endTime)
                                                        startTime = newStart
                                                        endTime = newStart.plusMinutes(duration) // Keep duration
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

                            Spacer(modifier = Modifier.height(24.dp))

                            // Repeat Row
                            OutlinedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Repeat,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Repeat",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = Color(0xFFF0F0F0),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "★ PRO",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Subtask Section
                            OutlinedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .border(1.5.dp, Color.LightGray, RoundedCornerShape(4.dp))
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "Add Subtask",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.LightGray
                                        )
                                    }
                                    Divider(color = Color.LightGray.copy(alpha = 0.3f))
                                    // Notes Input
                                    OutlinedTextField(
                                        value = notes,
                                        onValueChange = { notes = it },
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        placeholder = { 
                                            Text(
                                                "Add notes, meeting links or phone numbers...",
                                                color = Color.LightGray,
                                                style = MaterialTheme.typography.bodyMedium
                                            ) 
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent
                                        ),
                                        minLines = 3
                                    )
                                }
                            }
                        }

                        // Bottom Action Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
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
                                    .align(Alignment.CenterEnd)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEEEEEE),
                                    contentColor = Color.Gray
                                ),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Text(
                                    text = "Create Task",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
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
                ) { Text("OK") }
            }
        }
    }
}
