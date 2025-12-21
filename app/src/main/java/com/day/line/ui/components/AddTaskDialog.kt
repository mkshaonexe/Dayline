package com.day.line.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.day.line.data.Task
import com.day.line.ui.theme.DaylineOrange
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    selectedDate: String,
    taskToEdit: Task? = null,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit
) {
    // State
    var taskTitle by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var notes by remember { mutableStateOf(taskToEdit?.notes ?: "") }
    var isAllDay by remember { mutableStateOf(taskToEdit?.isAllDay ?: false) }
    
    // Date & Time Logic
    val initialDate = remember {
        try {
            if (taskToEdit != null) LocalDate.parse(taskToEdit.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            else LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (e: Exception) { LocalDate.now() }
    }
    var selectedDateState by remember { mutableStateOf(initialDate) }

    val now = LocalTime.now()
    val initialStartTime = remember {
        if (taskToEdit != null) try { LocalTime.parse(taskToEdit.startTime, DateTimeFormatter.ofPattern("HH:mm")) } catch (e: Exception) { now }
        else now
    }
    val initialEndTime = remember {
        if (taskToEdit != null) try { LocalTime.parse(taskToEdit.endTime, DateTimeFormatter.ofPattern("HH:mm")) } catch (e: Exception) { now.plusMinutes(30) }
        else now.plusMinutes(30)
    }
    var startTime by remember { mutableStateOf(initialStartTime) }
    var endTime by remember { mutableStateOf(initialEndTime) }

    // Pickers State
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Formatters
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

    val focusManager = LocalFocusManager.current

    // Dialog Container
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFFF9F9F9), // Light modern background
            floatingActionButton = {
                // Done Button (Floating)
                FloatingActionButton(
                    onClick = {
                        val finalTitle = if (taskTitle.isBlank()) "New Task" else taskTitle
                        val task = Task(
                            id = taskToEdit?.id ?: 0,
                            title = finalTitle,
                            date = selectedDateState.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            startTime = startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                            endTime = endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                            isAllDay = isAllDay,
                            notes = notes,
                            icon = "Work", // Default or selector
                            isCompleted = taskToEdit?.isCompleted ?: false
                        )
                        onSave(task)
                        onDismiss()
                    },
                    containerColor = DaylineOrange,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Check, "Save")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                // 1. Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (taskToEdit == null) "New task" else "Edit task",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp
                        ),
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 2. Task Name Input
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit, // Or a custom clipboard icon
                        contentDescription = null,
                        tint = Color.Gray, // Muted icon color
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        BasicTextField(
                            value = taskTitle,
                            onValueChange = { taskTitle = it },
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                            cursorBrush = SolidColor(DaylineOrange),
                            decorationBox = { innerTextField ->
                                Column {
                                    if (taskTitle.isEmpty()) {
                                        Text(
                                            text = "task name",
                                            style = TextStyle(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.LightGray
                                            )
                                        )
                                    } else {
                                        innerTextField()
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    // Underline
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(2.dp)
                                            .background(Color(0xFFFFCDD2)) // Light red/pink underline
                                    )
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 3. Date & Time Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Date
                    Row(
                        modifier = Modifier.clickable { showDatePicker = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Date",
                            tint = DaylineOrange, // Orange Accent
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = selectedDateState.format(dateFormatter),
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                            color = Color.Black
                        )
                    }

                    // Time
                    Row(
                        modifier = Modifier.clickable { showTimePicker = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = "Time",
                            tint = Color.LightGray, // As per reference (lighter blue/grey)
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${startTime.format(timeFormatter)} - ${endTime.format(timeFormatter)}",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 4. Repeat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "repeat",
                        style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.Repeat,
                        contentDescription = "Repeat",
                        tint = DaylineOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 5. Subtasks & Notes (Split View)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Left Column: add subtask
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "add subtask",
                            style = TextStyle(fontWeight = FontWeight.Black, fontSize = 18.sp),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Placeholder Subtasks (Visual only for now, or simple implementing)
                        // Implementing dynamic list might be complex for this snippet, keeping it visual
                        repeat(3) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.CheckBoxOutlineBlank,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(1.dp)
                                        .background(Color(0xFFFFCDD2))
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // Right Column: add note
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "add note",
                            style = TextStyle(fontWeight = FontWeight.Black, fontSize = 18.sp),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Note Input Area
                        BasicTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            textStyle = TextStyle(fontSize = 14.sp, color = Color.DarkGray),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                Column {
                                    if (notes.isEmpty()) {
                                        // Lines visualization
                                        repeat(3) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                                    .background(Color(0xFFFFCDD2))
                                            )
                                            Spacer(modifier = Modifier.height(19.dp)) // Line height spacing
                                        }
                                    } else {
                                        innerTextField()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Logic for DatePicker and TimePicker Dialogs
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateState.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                 TextButton(onClick = {
                     datePickerState.selectedDateMillis?.let {
                        val date = java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                        selectedDateState = date
                     }
                     showDatePicker = false
                 }) { Text("OK", color = DaylineOrange) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel", color = Color.Gray) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    if (showTimePicker) {
         Dialog(
             onDismissRequest = { showTimePicker = false },
             properties = DialogProperties(usePlatformDefaultWidth = false) // Full width for the overlay
         ) {
             Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .background(Color.Black.copy(alpha = 0.5f))
                     .clickable { showTimePicker = false },
                 contentAlignment = Alignment.Center
             ) {
                 // Prevent click on the picker from closing the dialog
                 Box(modifier = Modifier.clickable(enabled = false) {}) {
                     EnhancedTimePicker(
                         startTime = startTime,
                         endTime = endTime,
                         onStartTimeChange = { start ->
                             val duration = java.time.Duration.between(startTime, endTime).toMinutes()
                             val newEndTime = start.plusMinutes(if (duration > 0) duration else 30) // Maintain duration
                             startTime = start
                             endTime = newEndTime
                         },
                         onEndTimeChange = { end ->
                             endTime = end
                         },
                         onDismiss = { showTimePicker = false }
                     )
                 }
             }
         }
    }
}

