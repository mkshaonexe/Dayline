package com.day.line.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.day.line.data.Task
import com.day.line.ui.theme.DaylineOrange
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

    // ML Kit Entity Extraction Client (Optional/Future: could be injected)
    // For now, we rely on our robust keyword matcher which is very fast.
    
    LaunchedEffect(taskTitle) {
        if (!isManualIcon && taskToEdit == null) {
             val predicted = TaskIconUtils.predictIconName(taskTitle)
             if (predicted != "Edit") {
                 iconName = predicted
             }
        }
    }
    val focusRequester = remember { FocusRequester() }

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

    // Dynamic UI Color Logic
    // Default to LightPastelGreen if no color is selected, otherwise use the selected color
    val uiColor = selectedColor ?: com.day.line.ui.theme.LightPastelGreen

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFFF9F9F9), // Light background
            contentWindowInsets = WindowInsets.statusBars,
            floatingActionButton = {
                // Bottom-right Save Button (Dynamic Color with Check)
                FloatingActionButton(
                    onClick = { saveTask() },
                    containerColor = uiColor,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Check, "Save")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                // 1. Top Navigation Bar (Close & Top Save)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Close (X)
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close, 
                            contentDescription = "Close", 
                            tint = Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Right: Arrow Button (Save)
                    IconButton(
                        onClick = { saveTask() },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent)
                    ) {
                         // Approximating the "arrow in box" look or just a forward arrow
                         // Using a simple arrow icon, customizable if needed
                         Icon(
                             Icons.AutoMirrored.Filled.ArrowForward,
                             contentDescription = "Save",
                             tint = Color(0xFF607D8B), // Slate-ish blue/grey like reference
                             modifier = Modifier
                                 .size(32.dp)
                                 .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                 .padding(4.dp)
                         )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Header Title
                Text(
                    text = if (taskToEdit == null) "New task" else "Edit task",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 36.sp
                    ),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 3. Task Name Input
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { showColorPicker = true }
                            .padding(4.dp)
                    ) {
                        if (selectedColor != null) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(selectedColor!!, androidx.compose.foundation.shape.CircleShape)
                                    .border(1.dp, Color.Gray, androidx.compose.foundation.shape.CircleShape)
                            )
                        } else {
                            // Default rainbow wheel or similar icon for "pick color"
                            Canvas(modifier = Modifier.size(24.dp)) {
                                val colors = listOf(Color.Red, Color.Green, Color.Blue)
                                drawCircle(
                                    brush = androidx.compose.ui.graphics.Brush.sweepGradient(colors)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = TaskIconUtils.getIconByName(iconName),
                        contentDescription = "Icon",
                        tint = uiColor,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { showIconPicker = true }
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        BasicTextField(
                            value = taskTitle,
                            onValueChange = { taskTitle = it },
                            modifier = Modifier.focusRequester(focusRequester),
                            textStyle = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                            cursorBrush = SolidColor(uiColor),
                            decorationBox = { innerTextField ->
                                Column {
                                    if (taskTitle.isEmpty()) {
                                        Text(
                                            text = "task name",
                                            style = TextStyle(
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.LightGray
                                            )
                                        )
                                    } else {
                                        innerTextField()
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(2.dp)
                                            .background(Color(0xFFFFCDD2))
                                    )
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 4. Date & Time
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
                            tint = uiColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = selectedDateState.format(dateFormatter),
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp),
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
                            tint = Color.LightGray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${startTime.format(timeFormatter)} - ${endTime.format(timeFormatter)}",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 5. Repeat (Centered)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "repeat",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Repeat,
                        contentDescription = "Repeat",
                        tint = uiColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 6. Subtasks & Notes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Left: Subtasks
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "add subtask",
                            style = TextStyle(fontWeight = FontWeight.Black, fontSize = 16.sp),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // New Subtask Input (Always visible at top or bottom? Reference shows just '+' and line)
                        // Implementing '+' then input line like reference
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                tint = uiColor,
                                modifier = Modifier
                                    .size(20.dp)
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
                                modifier = Modifier.weight(1f),
                                decorationBox = { innerTextField ->
                                    Column {
                                        innerTextField()
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(Color(0xFFFFCDD2))
                                        )
                                    }
                                }
                            )
                        }

                        // List Existing
                        if (subtasks.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            subtasks.forEachIndexed { index, subtask ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.CheckBoxOutlineBlank, null, tint=Color.Gray, modifier=Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(subtask, style = TextStyle(fontSize = 14.sp), modifier=Modifier.weight(1f))
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint=Color.Red.copy(0.5f),
                                        modifier=Modifier.size(16.dp).clickable { subtasks.removeAt(index) }
                                    )
                                }
                            }
                        }
                    }

                    // Right: Notes
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "add note",
                            style = TextStyle(fontWeight = FontWeight.Black, fontSize = 16.sp),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Ruled Lines Effect
                        BasicTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            textStyle = TextStyle(fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                Box {
                                    // Background Lines
                                    Column {
                                        repeat(4) { // Draw a few lines
                                            Spacer(modifier = Modifier.height(19.dp)) // Approximate line height
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                                    .background(Color(0xFFFFCDD2))
                                            )
                                        }
                                    }
                                    // Content
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Pickers (Unchanged logic, just ensure they show up)
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

    if (showIconPicker) {
        Dialog(onDismissRequest = { showIconPicker = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select Icon", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                        columns = androidx.compose.foundation.lazy.grid.GridCells.Adaptive(minSize = 48.dp),
                        modifier = Modifier.height(300.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(TaskIconUtils.AvailableIcons.size) { index ->
                            val (name, icon) = TaskIconUtils.AvailableIcons[index]
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable {
                                        iconName = name
                                        isManualIcon = true
                                        showIconPicker = false
                                    }
                                    .background(
                                        if (iconName == name) uiColor.copy(alpha = 0.2f) else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (iconName == name) uiColor else Color.LightGray,
                                        RoundedCornerShape(8.dp)
                                    )
                            ) {
                                Icon(icon, contentDescription = name, tint = if (iconName == name) uiColor else Color.Gray)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = { showIconPicker = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cancel", color = uiColor)
                    }
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
                         // null (default) option
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
