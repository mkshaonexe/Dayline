package com.day.line.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.day.line.ui.components.AddTaskDialog
import com.day.line.ui.components.BottomNavItem
import com.day.line.ui.components.LiquidBottomNavigation
import com.day.line.ui.theme.DaylineOrange
import com.day.line.ui.journal.JournalScreen
import com.day.line.ui.profile.ProfileScreen
import com.day.line.ui.settings.SettingsScreen

@Composable
fun HomeScreen(
    viewModel: TaskViewModel = hiltViewModel()
) {
    val items = listOf(
        BottomNavItem.Journaling,
        BottomNavItem.Dayline,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )
    
    // Default to Dayline as it's the core feature
    var currentRoute by remember { mutableStateOf(BottomNavItem.Dayline.route) }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    val selectedDate by viewModel.selectedDate.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF5F5F5),

        floatingActionButton = {
            // Only show FAB on Dayline screen
            if (currentRoute == BottomNavItem.Dayline.route) {
                FloatingActionButton(
                    onClick = { showAddTaskDialog = true },
                    containerColor = DaylineOrange,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(bottom = 90.dp) // Lift FAB above the floating nav bar
                        .size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentRoute, label = "NavigationCrossfade") { route ->
                when (route) {
                    BottomNavItem.Journaling.route -> JournalScreen()
                    BottomNavItem.Dayline.route -> DaylineScreen(viewModel)
                    BottomNavItem.Profile.route -> ProfileScreen()
                    BottomNavItem.Settings.route -> SettingsScreen()
                    else -> DaylineScreen(viewModel)
                }
            }
            
            // Floating Bottom Navigation Overlay
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                LiquidBottomNavigation(
                    items = items,
                    currentRoute = currentRoute,
                    onItemClick = { currentRoute = it.route }
                )
            }

            // Add Task Dialog (Overlay)
            if (showAddTaskDialog) {
                AddTaskDialog(
                    selectedDate = selectedDate,
                    onDismiss = { showAddTaskDialog = false },
                    onSave = { task ->
                        viewModel.addTask(task)
                    }
                )
            }
        }
    }
}
