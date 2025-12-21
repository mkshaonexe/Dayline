package com.day.line.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var showNoteInput by remember { mutableStateOf(false) }
    
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
    val minute = now.minute
    val roundedMinute = ((minute + 14) / 15) * 15
    val baseTime = now.withMinute(0).plusMinutes(roundedMinute.toLong())
    
    var startTime by remember { mutableStateOf(baseTime) }
    var endTime by remember { mutableStateOf(baseTime.plusMinutes(15)) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showEnhancedTimePicker by remember { mutableStateOf(false) }
    
    val dateDisplay = LocalDate.of(selectedYear, selectedMonth, selectedDay)
    // Format: "sun, 18 dec 2025"
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a") // 3:04 pm

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        // Full screen white background container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)) // Light grey/white paper-like background
                .windowInsetsPadding(WindowInsets.safeDrawing.union(WindowInsets.ime)) // Safe from system bars and keyboard
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Scrollable Content Region
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // 1. Header: Close Icon + "New Task"
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = buildAnnotatedString {
                                append("New ")
                                withStyle(SpanStyle(color = DaylineOrange)) {
                                    append("Task")
                                }
                            },
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            ),
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // 2. "What?" Prompt & Title Input
                    Text(
                        text = "What?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    BasicTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Red Underline
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE57373)) // Light red line
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // 3. Central Details List
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) { 
                        // ... Content ...
                        // Date
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { showDatePicker = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Date",
                                tint = DaylineOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = dateDisplay.format(dateFormatter).lowercase(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = Color.Black
                            )
                        }

                        // Time (if not all day)
                        if (!isAllDay) {
                            Text(
                                text = "${startTime.format(timeFormatter).lowercase()} - ${endTime.format(timeFormatter).lowercase()}",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Black, // Very bold
                                    fontSize = 18.sp
                                ),
                                color = Color.Black,
                                modifier = Modifier.clickable { showEnhancedTimePicker = true }
                            )
                        }

                        // Repeat
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { /* TODO: Implement repeat logic */ }
                        ) {
                            Text(
                                text = "repeat",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Repeat",
                                tint = DaylineOrange, // Orange accent icon
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Add Subtask
                        Text(
                            text = "add subtask",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = Color.Black,
                            modifier = Modifier.clickable { /* TODO: Subtask */ }
                        )

                        // Add Note
                        if (showNoteInput) {
                            OutlinedTextField(
                                value = notes,
                                onValueChange = { notes = it },
                                placeholder = { Text("Enter note...", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(0.8f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DaylineOrange,
                                    unfocusedBorderColor = Color.LightGray
                                )
                            )
                        } else {
                            Text(
                                text = "add note",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp
                                ),
                                color = Color.Black, // Dark/Bold
                                modifier = Modifier.clickable { showNoteInput = true }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                } // End of Scrollable Content

                // 4. "add task" Button (Sticky at Bottom)
                Button(
                    onClick = {
                        val finalTitle = if (taskTitle.isBlank()) "New Task" else taskTitle
                        val task = Task(
                            title = finalTitle,
                            date = Task.formatDate(selectedYear, selectedMonth, selectedDay),
                            startTime = Task.formatTime(startTime.hour, startTime.minute),
                            endTime = Task.formatTime(endTime.hour, endTime.minute),
                            isAllDay = isAllDay,
                            notes = notes,
                            icon = "Work" // Default icon
                        )
                        onSave(task)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8A80), // Light Salmon/Red color from image
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(4.dp) // Boxy shape
                ) {
                    Text(
                        text = "add task",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                }
            }
        }
    }
    
    // ... DatePicker and EnhancedTimePicker logic (unchanged) ...
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = LocalDate.of(selectedYear, selectedMonth, selectedDay)
                .atStartOfDay(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {},
            dismissButton = {}
        ) {
            DatePicker(state = datePickerState, showModeToggle = false)
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = java.time.Instant.ofEpochMilli(millis).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                        selectedYear = date.year
                        selectedMonth = date.monthValue
                        selectedDay = date.dayOfMonth
                    }
                    showDatePicker = false
                }) { Text("OK", color = DaylineOrange) }
            }
        }
    }

    if (showEnhancedTimePicker) {
        // Overlay for EnhancedTimePicker to make it modal-like or just show inline?
        // Dialog already provided by EnhancedTimePicker logic inside? 
        // No, we need to wrap it in a Dialog since we are inside a fullscreen Dialog.
        Dialog(
            onDismissRequest = { showEnhancedTimePicker = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha=0.5f)).clickable{showEnhancedTimePicker=false},
                contentAlignment = Alignment.Center
            ) {
                 Box(modifier = Modifier.fillMaxWidth(0.9f).clickable(enabled=false){}) {
                     EnhancedTimePicker(
                        startTime = startTime,
                        endTime = endTime,
                        onStartTimeChange = { start ->
                             val duration = java.time.Duration.between(startTime, endTime).toMinutes()
                             startTime = start
                             endTime = start.plusMinutes(duration)
                        },
                        onEndTimeChange = { end -> endTime = end },
                        onDismiss = { showEnhancedTimePicker = false }
                    )
                 }
            }
        }
    }
}
