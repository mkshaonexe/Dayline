package com.day.line.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
    var isBottomBarVisible by remember { mutableStateOf(true) }
    val selectedDate by viewModel.selectedDate.collectAsState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // If scrolling down (delta < 0), hide. If scrolling up (delta > 0), show.
                if (available.y < -5f) {
                    isBottomBarVisible = false
                } else if (available.y > 5f) {
                    isBottomBarVisible = true
                }
                return Offset.Zero
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        // FAB moved to main content to coordinate animation with Bottom Bar
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(nestedScrollConnection)
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
            
            // Floating Bottom Navigation Overlay & FAB
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isBottomBarVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(durationMillis = 800)),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(durationMillis = 800, easing = FastOutLinearInEasing)
                    ) + fadeOut(animationSpec = tween(durationMillis = 800))
                ) {
                    LiquidBottomNavigation(
                        items = items,
                        currentRoute = currentRoute,
                        onItemClick = { currentRoute = it.route }
                    )
                }
            }

            // Floating Action Button (FAB) - Also animated
            if (currentRoute == BottomNavItem.Dayline.route) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 90.dp) // Adjusted padding for overlay FAB
                ) {
                    // Radiant Gradient FAB
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isBottomBarVisible,
                        enter = scaleIn(animationSpec = tween(600)) + fadeIn(animationSpec = tween(600)),
                        exit = scaleOut(animationSpec = tween(600)) + fadeOut(animationSpec = tween(600))
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp) // Slightly larger for impact
                                .shadow(12.dp, CircleShape, spotColor = DaylineOrange)
                                .clip(CircleShape)
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(com.day.line.ui.theme.NeonOrange, com.day.line.ui.theme.DaylineOrange)
                                    )
                                )
                                .clickable { showAddTaskDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Task",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
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
