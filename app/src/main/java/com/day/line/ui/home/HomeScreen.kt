package com.day.line.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.day.line.ui.components.BottomNavItem
import com.day.line.ui.components.CalendarStrip
import com.day.line.ui.components.LiquidBottomNavigation
import com.day.line.ui.components.TimelineNode
import com.day.line.ui.components.TimelineNodeType
import com.day.line.ui.theme.DaylineOrange
import com.day.line.ui.theme.SoftGray
import com.day.line.ui.theme.SoftTeal
import com.day.line.ui.theme.WarmPink

@Composable
fun HomeScreen() {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Analytics,
        BottomNavItem.Tasks,
        BottomNavItem.Profile
    )
    var currentRoute by remember { mutableStateOf("home") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF5F5F5), // Light background
        bottomBar = {
            LiquidBottomNavigation(
                items = items,
                currentRoute = currentRoute,
                onItemClick = { currentRoute = it.route }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CalendarStrip()
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color.White) // Card-like background for timeline
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                TimelineNode(
                    time = "08:00 AM",
                    title = "Rise and Shine",
                    type = TimelineNodeType.START,
                    color = DaylineOrange,
                    icon = Icons.Default.WbSunny
                )
                
                TimelineNode(
                    time = "",
                    title = "Reflect on the respite",
                    subtitle = "Hope that pause was refreshing.",
                    type = TimelineNodeType.GAP,
                    color = SoftGray
                )
                
                TimelineNode(
                    time = "10:00 PM",
                    title = "Wind Down",
                    type = TimelineNodeType.END,
                    color = SoftTeal,
                    icon = Icons.Default.Bedtime,
                    isLast = true
                )
                
                Spacer(modifier = Modifier.height(100.dp)) // Bottom padding
            }
        }
    }
}
