package com.day.line.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // for fillMaxSize, padding, etc
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.day.line.data.Task
import com.day.line.ui.util.TaskIconUtils
import org.json.JSONArray
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
    
    // Subtasks State
    val initialSubtasks = remember(taskToEdit) {
        try {
            if (!taskToEdit?.subtasks.isNullOrEmpty()) {
                val jsonArray = JSONArray(taskToEdit.subtasks)
                List(jsonArray.length()) { jsonArray.getString(it) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    val subtasks = remember { mutableStateListOf<String>().apply { addAll(initialSubtasks) } }
    var newSubtaskText by remember { mutableStateOf("") }

    // Icon State
    var iconName by remember { mutableStateOf(taskToEdit?.icon ?: "Edit") }
    var isManualIcon by remember { mutableStateOf(taskToEdit != null) } // Assume manual if editing, or false if new
    var showIconPicker by remember { mutableStateOf(false) }

    // Real-time Icon Prediction
    LaunchedEffect(taskTitle) {
        if (!isManualIcon && taskTitle.isNotEmpty()) {
            val predicted = TaskIconUtils.predictIconName(taskTitle)
            if (predicted != "Edit") {
                iconName = predicted
            }
        }
    }
    
    // Color State
    var selectedColor by remember { mutableStateOf(taskToEdit?.color?.let { Color(it) }) }
    var showColorPicker by remember { mutableStateOf(false) }
    
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

    val focusRequester = remember { FocusRequester() }
    val subtaskFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (taskToEdit == null) {
            focusRequester.requestFocus()
        }
    }

    // Helper to save task
    fun saveTask() {
        val finalTitle = if (taskTitle.isBlank()) "New Task" else taskTitle
        
        val subtasksJson = try {
            val jsonArray = JSONArray()
            subtasks.forEach { jsonArray.put(it) }
            jsonArray.toString()
        } catch (e: Exception) { "[]" }

        val task = Task(
            id = taskToEdit?.id ?: 0,
            title = finalTitle,
            date = selectedDateState.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            startTime = startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            endTime = endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            isAllDay = isAllDay,
            notes = notes,
            subtasks = subtasksJson,
            icon = iconName,
            color = selectedColor?.toArgb(),
            isCompleted = taskToEdit?.isCompleted ?: false
        )
        onSave(task)
        onDismiss()
    }

    // UI Colors
    val uiColor = selectedColor ?: com.day.line.ui.theme.LightPastelGreen
    val dashedGreen = Color(0xFFC8E6C9)
    val dashedRed = Color(0xFFFFCDD2)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.White,
            contentWindowInsets = WindowInsets.statusBars,
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // 1. Modern Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp), // Add spacing below header
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Close (Cross)
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.Transparent, androidx.compose.foundation.shape.CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Close, 
                                contentDescription = "Close", 
                                tint = Color.Black.copy(alpha = 0.7f),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Center: Title
                        Text(
                            text = if (taskToEdit == null) "Add Task" else "Edit Task",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color.Black.copy(alpha = 0.8f)
                        )

                        // Right: Add Button (Pill shaped)
                        Button(
                            onClick = { saveTask() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = uiColor,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(
                                text = if (taskToEdit == null) "Add" else "Save",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // 3. Task Name Input
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Color Picker (Gradient Circle) - Smaller & Cuter
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { showColorPicker = true }
                        ) {
                            if (selectedColor != null) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(selectedColor!!, androidx.compose.foundation.shape.CircleShape)
                                )
                            } else {
                                // Gradient for "Pick Color"
                                Canvas(modifier = Modifier.size(24.dp)) {
                                    val colors = listOf(Color(0xFF2196F3), Color(0xFFE91E63), Color(0xFFFF9800))
                                    drawCircle(
                                        brush = androidx.compose.ui.graphics.Brush.linearGradient(colors)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))

                        // Icon (Dynamic: Manual or Auto-predicted) - Smaller & Cuter
                        Icon(
                            imageVector = TaskIconUtils.getIconByName(iconName),
                            contentDescription = "Task Icon",
                            tint = uiColor,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { showIconPicker = true }
                                .padding(2.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Input Field
                        BasicTextField(
                            value = taskTitle,
                            onValueChange = { taskTitle = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            ),
                            cursorBrush = SolidColor(uiColor),
                            decorationBox = { innerTextField ->
                                Column {
                                    Box {
                                        if (taskTitle.isEmpty()) {
                                            Text(
                                                text = "task name",
                                                style = TextStyle(
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color.LightGray
                                                )
                                            )
                                        }
                                        innerTextField()
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    // Dashed Green Line
                                    DashedDivider(color = dashedGreen)
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // 4. Date & Time Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp) // Space between Date and Time
                    ) {
                        // Date
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { showDatePicker = true }
                        ) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = "Date",
                                tint = Color(0xFF78909C), // Blue Grey
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedDateState.format(dateFormatter),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold, 
                                    fontSize = 16.sp,
                                    color = Color(0xFF2C3E50)
                                )
                            )
                        }

                        // Time
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { showTimePicker = true }
                        ) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = "Time",
                                tint = Color(0xFF78909C),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${startTime.format(timeFormatter)} - ${endTime.format(timeFormatter)}",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold, 
                                    fontSize = 16.sp,
                                    color = Color(0xFF2C3E50)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // 5. Repeat
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* Toggle Repeat Logic */ },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "repeat",
                            style = TextStyle(
                                fontWeight = FontWeight.Medium, 
                                fontSize = 16.sp,
                                color = Color(0xFF78909C)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Repeat,
                            contentDescription = "Repeat",
                            tint = Color(0xFF78909C),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    // 6. Subtasks & Notes Columns
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // --- Left: Subtasks ---
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "add subtask",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold, 
                                    fontSize = 14.sp,
                                    color = Color(0xFF546E7A)
                                ),
                                modifier = Modifier.clickable { subtaskFocusRequester.requestFocus() }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Input Row
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add Subtask",
                                    tint = com.day.line.ui.theme.DaylineGreen, // Green like reference '+'
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            if (newSubtaskText.isNotBlank()) {
                                                subtasks.add(newSubtaskText)
                                                newSubtaskText = ""
                                            }
                                        }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                BasicTextField(
                                    value = newSubtaskText,
                                    onValueChange = { newSubtaskText = it },
                                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                    modifier = Modifier
                                        .weight(1f)
                                        .focusRequester(subtaskFocusRequester),
                                    cursorBrush = SolidColor(uiColor),
                                    decorationBox = { innerTextField ->
                                        Column {
                                            Box {
                                                if (newSubtaskText.isEmpty()) {
                                                    Text("Add subtask...", style = TextStyle(fontSize = 14.sp, color = Color.LightGray))
                                                }
                                                innerTextField()
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            DashedDivider(color = dashedRed)
                                        }
                                    }
                                )
                            }
                            
                            // Existing Subtasks
                            if (subtasks.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                subtasks.forEachIndexed { index, subtask ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(Color.Gray, androidx.compose.foundation.shape.CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(subtask, style = TextStyle(fontSize = 14.sp, color = Color.DarkGray), modifier=Modifier.weight(1f))
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Remove",
                                            tint=Color.LightGray,
                                            modifier=Modifier.size(14.dp).clickable { subtasks.removeAt(index) }
                                        )
                                    }
                                }
                            }
                        }

                        // --- Right: Notes ---
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "add note",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold, 
                                    fontSize = 14.sp,
                                    color = Color(0xFF546E7A)
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Notes Input with Multi-line Dashed Background
                            BasicTextField(
                                value = notes,
                                onValueChange = { notes = it },
                                textStyle = TextStyle(fontSize = 14.sp, color = Color.DarkGray, lineHeight = 24.sp),
                                modifier = Modifier.fillMaxWidth(),
                                cursorBrush = SolidColor(uiColor),
                                decorationBox = { innerTextField ->
                                    Box {
                                        // Draw dashed lines for "notebook" feel
                                        Column {
                                            repeat(4) { 
                                                Spacer(modifier = Modifier.height(23.dp)) // Line height matching text
                                                DashedDivider(color = dashedRed)
                                            }
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }
                    }
                    
                    // Extra space at bottom
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Pickers (Unchanged logic)
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
                 }) { Text("OK", color = uiColor) }
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
             properties = DialogProperties(usePlatformDefaultWidth = false)
         ) {
             Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .background(Color.Black.copy(alpha = 0.5f))
                     .clickable { showTimePicker = false },
                 contentAlignment = Alignment.Center
             ) {
                 Box(modifier = Modifier.clickable(enabled = false) {}) {
                     EnhancedTimePicker(
                         startTime = startTime,
                         endTime = endTime,
                         onStartTimeChange = { start ->
                             val duration = java.time.Duration.between(startTime, endTime).toMinutes()
                             val newEndTime = start.plusMinutes(if (duration > 0) duration else 30)
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

    if (showColorPicker) {
        Dialog(onDismissRequest = { showColorPicker = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val colors = com.day.line.ui.theme.TaskPalette + listOf(
                         com.day.line.ui.theme.DaylinePink,
                         com.day.line.ui.theme.NeonPink,
                         com.day.line.ui.theme.LightPastelGreen
                    )
                    
                    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                        columns = androidx.compose.foundation.lazy.grid.GridCells.Adaptive(minSize = 48.dp),
                        modifier = Modifier.height(200.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable {
                                        selectedColor = null
                                        showColorPicker = false
                                    }
                                    .border(2.dp, if (selectedColor == null) uiColor else Color.LightGray, androidx.compose.foundation.shape.CircleShape)
                                    .padding(4.dp)
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val canvasWidth = size.width
                                    val canvasHeight = size.height
                                    drawLine(
                                        start = Offset(0f, canvasHeight),
                                        end = Offset(canvasWidth, 0f),
                                        color = Color.Red,
                                        strokeWidth = 5f
                                    )
                                }
                            }
                        }
                        
                        items(colors.size) { index ->
                            val color = colors[index]
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable {
                                        selectedColor = color
                                        showColorPicker = false
                                    }
                                    .background(color, androidx.compose.foundation.shape.CircleShape)
                                    .border(
                                        2.dp, 
                                        if (selectedColor == color) Color.Black else Color.Transparent, 
                                        androidx.compose.foundation.shape.CircleShape
                                    )
                            ) {}
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = { showColorPicker = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cancel", color = uiColor)
                    }
                }
            }
        }
    }
}

@Composable
fun DashedDivider(
    color: Color = Color.LightGray,
    thickness: androidx.compose.ui.unit.Dp = 1.dp,
    dashWidth: Float = 10f,
    dashGap: Float = 10f
) {
    Canvas(modifier = Modifier.fillMaxWidth().height(thickness)) {
        val pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(dashWidth, dashGap), 0f)
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = thickness.toPx(),
            pathEffect = pathEffect
        )
    }
}
