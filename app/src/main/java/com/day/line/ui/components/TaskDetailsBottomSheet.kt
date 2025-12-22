package com.day.line.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.day.line.data.Task
import com.day.line.ui.theme.DaylinePink
import com.day.line.ui.theme.TextDark
import com.day.line.ui.theme.TextLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsBottomSheet(
    task: Task,
    color: Color = DaylinePink, // Default fallback
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    onEdit: () -> Unit,
    onToggleCompletion: () -> Unit,
    onSubtaskChange: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            // Apple-style drag handle
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.5f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp) // Safety padding
        ) {
            // Header Section: Icon + Date/Time + Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Task Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = com.day.line.ui.util.TaskIconUtils.getIconByName(task.icon),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    // Date & Time
                    Text(
                        text = "${task.getDisplayDate()}, ${task.getDisplayTime()} (${task.getDuration()})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextLight,
                        fontSize = 14.sp
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))

                    // Title
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                }
            }



            Spacer(modifier = Modifier.height(24.dp))
            
            // Notes Section
            if (task.notes.isNotEmpty()) {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = color),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = task.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Subtasks Section
            if (task.subtasks.isNotEmpty() && task.subtasks != "[]") {
                val subtasks: List<SubtaskUiModel> = remember(task.subtasks) {
                     try {
                         val jsonArray = org.json.JSONArray(task.subtasks)
                         List(jsonArray.length()) { index ->
                             val item = jsonArray.get(index)
                             if (item is org.json.JSONObject) {
                                  SubtaskUiModel(item.getString("text"), item.optBoolean("isCompleted", false))
                             } else {
                                  // Backward compatibility for plain strings
                                  SubtaskUiModel(item.toString(), false)
                             }
                         }
                     } catch (e: Exception) { emptyList<SubtaskUiModel>() }
                }

                if (subtasks.isNotEmpty()) {
                     Text(
                        text = "Subtasks",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = color),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    subtasks.forEachIndexed { index, subtaskItem ->
                        val text = subtaskItem.text
                        val isCompleted = subtaskItem.isCompleted
                        Row(
                             verticalAlignment = Alignment.CenterVertically, 
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .clickable {
                                     // Toggle logic
                                     val newSubtasks = subtasks.toMutableList()
                                     newSubtasks[index] = subtaskItem.copy(isCompleted = !isCompleted)
                                     
                                     // Serialize back to JSON
                                     val jsonArray = org.json.JSONArray()
                                     newSubtasks.forEach { item ->
                                         val obj = org.json.JSONObject()
                                         obj.put("text", item.text)
                                         obj.put("isCompleted", item.isCompleted)
                                         jsonArray.put(obj)
                                     }
                                     onSubtaskChange(jsonArray.toString())
                                 }
                                 .padding(vertical = 6.dp)
                        ) {
                             Icon(
                                 if (isCompleted) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank, 
                                 contentDescription = null, 
                                 tint = if (isCompleted) color else Color.Gray, 
                                 modifier = Modifier.size(20.dp)
                             )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = text, 
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    textDecoration = if (isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
                                    color = if (isCompleted) Color.Gray else TextDark
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp)) // Reduced spacer as content was added

            // Action Buttons Row (Delete, Duplicate, Incomplete)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionButton(
                    icon = Icons.Default.Delete,
                    label = "Delete",
                    color = color,
                    modifier = Modifier.weight(1f),
                    onClick = onDelete
                )

                ActionButton(
                    icon = Icons.Default.ContentCopy,
                    label = "Duplicate",
                    color = color, // Using color for consistency
                    modifier = Modifier.weight(1f),
                    onClick = onDuplicate
                )

                ActionButton(
                    icon = if (task.isCompleted) Icons.Default.Close else Icons.Default.Check, // Show 'Close' (Incomplete) if completed
                    label = if (task.isCompleted) "Incomplete" else "Complete",
                    color = color,
                    modifier = Modifier.weight(1f),
                    onClick = onToggleCompletion
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Edit Button (Full Width)
            Button(
                onClick = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Task",
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Task",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1f) // Square/Boxy
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = color
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Helper to resolve icon (duplicated from HomeScreen but needed here)
// Ideally this should be in a shared utility
private fun getIconByName(name: String): ImageVector {
    return when (name) {
        "Notifications" -> Icons.Default.Notifications
        "List" -> Icons.Default.List
        "Face" -> Icons.Default.Face
        "PlayArrow" -> Icons.Default.PlayArrow
        "Bedtime" -> Icons.Default.Bedtime
        "Work" -> Icons.Default.Work
        "FitnessCenter" -> Icons.Default.FitnessCenter
        "LocalCafe" -> Icons.Default.LocalCafe
        "Create" -> Icons.Default.Create
        "Code" -> Icons.Default.Code
        "MusicNote" -> Icons.Default.MusicNote
        "Book" -> Icons.Default.Book
        else -> Icons.Default.Star
    }
}
