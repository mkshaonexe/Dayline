package com.day.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.day.myapplication.ui.theme.*
import com.day.myapplication.components.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Ensure Scaffold is not nested if DaylineScreen provides its own Scaffold, 
                // but DaylineScreen uses Scaffold.
                // The original code had a Scaffold wrapping Greeting. 
                // I will remove the outer Scaffold and let DaylineScreen handle the screen structure.
                DaylineScreen()
            }
        }
    }
}

@Composable
fun DaylineScreen() {
    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = PastelRed,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        subtitle = "A well-deserved break.", // Placeholder text from image looks like this or similar
                        icon = Icons.Default.Notifications, // Alarm icon
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
                        color = PastelActionBlue // Light blue/grey
                    )
                }
                item {
                    TimelineNode(
                        time = "09:00",
                        endTime = "09:30",
                        duration = "30 min",
                        title = "Get Ready",
                        icon = Icons.Default.Face, // Comb icon unavailable, Face is close conceptually
                        color = PastelOrange
                    )
                }
                item {
                    TimelineNode(
                        time = "11:24",
                        duration = "20m remaining",
                        title = "Filming",
                        subtitle = "Downtime—recharge complete.", // Text actually belongs to previous usually but placing here for visual match
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
}

@Preview(showBackground = true)
@Composable
fun DaylinePreview() {
    MyApplicationTheme {
        DaylineScreen()
    }
}