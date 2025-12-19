package com.day.line.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.day.line.ui.components.CalendarStrip
import com.day.line.ui.components.TaskTimelineNode
import com.day.line.ui.components.TimelineNode
import com.day.line.ui.components.TimelineNodeType
import com.day.line.ui.theme.DaylineOrange
import com.day.line.ui.theme.TextGrey
import com.day.line.ui.theme.SoftTeal
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import com.day.line.ui.theme.CreamBackground

@Composable
fun DaylineScreen(
    viewModel: TaskViewModel = hiltViewModel()
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val tasks by viewModel.tasksForSelectedDate.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CalendarStrip(
            selectedDate = selectedDate,
            onDateSelected = { date ->
                viewModel.selectDate(date)
            }
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Rise and Shine (8:00 AM)
            TimelineNode(
                time = "08:00 AM",
                title = "Rise and Shine",
                type = TimelineNodeType.START,
                color = DaylineOrange,
                icon = Icons.Default.WbSunny
            )
            
            // User Tasks - sorted by time
            if (tasks.isEmpty()) {
                // Empty state - show reflection message
                TimelineNode(
                    time = "",
                    title = "Reflect on the respite",
                    subtitle = "Tap + to add tasks",
                    type = TimelineNodeType.GAP,
                    color = TextGrey
                )
            } else {
                // Render user tasks
                tasks.forEachIndexed { index, task ->
                    val displayTime = try {
                        val time = LocalTime.parse(task.startTime, DateTimeFormatter.ofPattern("HH:mm"))
                        time.format(DateTimeFormatter.ofPattern("h:mm a"))
                    } catch (e: Exception) {
                        task.startTime
                    }
                    
                    TaskTimelineNode(
                        time = displayTime,
                        title = task.title,
                        duration = task.getDuration(),
                        notes = task.notes,
                        color = DaylineOrange,
                        isLast = false
                    )
                }
            }
            
            // Wind Down (10:00 PM)
            TimelineNode(
                time = "10:00 PM",
                title = "Wind Down",
                type = TimelineNodeType.END,
                color = SoftTeal,
                icon = Icons.Default.Bedtime,
                isLast = true
            )
            
            // Bottom padding for fab and nav bar
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
