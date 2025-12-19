package com.day.line.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.day.line.ui.components.CalendarStrip
import com.day.line.ui.components.TimelineNode
import com.day.line.ui.theme.*

@Composable
fun DaylineScreen(
    viewModel: TaskViewModel // Kept for structure, unused for static UI
) {
    // Note: Scaffold was removed here to let HomeScreen handle the main Scaffold/Navigation.
    // Based on the reference code, this screen just provides the content.
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp) // Add padding to avoid overlap with floating bottom bar
    ) {
        CalendarStrip()

        // Visual Section Separator
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                TimelineNode(
                    time = "05:30",
                    title = "Wake up!",
                    subtitle = "A well-deserved break.",
                    icon = Icons.Default.Notifications,
                    color = PastelRed
                )
            }
            item {
                TimelineNode(
                    time = "08:30",
                    endTime = "09:00",
                    duration = "30 min",
                    title = "Plan Day",
                    icon = Icons.Default.List,
                    color = PastelActionBlue
                )
            }
            item {
                TimelineNode(
                    time = "09:00",
                    endTime = "09:30",
                    duration = "30 min",
                    title = "Get Ready",
                    icon = Icons.Default.Face,
                    color = PastelOrange
                )
            }
            item {
                TimelineNode(
                    time = "11:24",
                    duration = "20m remaining",
                    title = "Filming",
                    subtitle = "Downtime—recharge complete.",
                    icon = Icons.Default.PlayArrow,
                    color = PastelGreen,
                    isLast = true
                )
            }
            // Extra space at bottom
            item { 
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
