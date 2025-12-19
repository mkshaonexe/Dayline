package com.day.line.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.day.line.ui.theme.GlassBlack

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object Home : BottomNavItem("Home", Icons.Default.Home, "home")
    object Analytics : BottomNavItem("Stats", Icons.Default.PieChart, "stats") // Placeholder icon
    object Tasks : BottomNavItem("Tasks", Icons.Default.CheckCircle, "tasks")
    object Profile : BottomNavItem("Profile", Icons.Default.Person, "profile")
}

@Composable
fun LiquidBottomNavigation(
    items: List<BottomNavItem>,
    currentRoute: String,
    onItemClick: (BottomNavItem) -> Unit
) {
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }.takeIf { it != -1 } ?: 0

    Box(
        modifier = Modifier
            .padding(24.dp) // More margin for floating effect
            .fillMaxWidth()
            .height(64.dp) // Smaller height
            .clip(RoundedCornerShape(32.dp)) // Adjusted corners
            .background(
                color = Color.Black.copy(alpha = 0.6f) // Darker Glass background
            )
            .padding(4.dp) // Inner padding
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                
                // Animate weight/size for liquid effect
                val weight by animateFloatAsState(
                    targetValue = if (isSelected) 1.5f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy, // Smoother, less bounce
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "weight"
                )

                Box(
                    modifier = Modifier
                        .weight(weight)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(32.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onItemClick(item) }
                        .then(
                            if (isSelected) {
                                Modifier.background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF4CAF50).copy(alpha = 0.8f), // Richer Green
                                            Color(0xFF81C784).copy(alpha = 0.6f)  // Lighter shine
                                        )
                                    )
                                )
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        // Expanded view with text
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            // Text hidden on very small screens or just shown next to icon
                            // For this specific design, it shows text below or beside. 
                            // The reference image shows "Home" text below icon or just icon. 
                            // Let's stick to the image style: Icon + Text for selected
                            
                            Box(modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = item.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        // Icon only
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewLiquidBottomNav() {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Analytics,
        BottomNavItem.Tasks,
        BottomNavItem.Profile
    )
    var currentRoute by remember { mutableIntStateOf(0) }
    
    Box(modifier = Modifier.background(Color.Black).padding(20.dp)) {
        LiquidBottomNavigation(
            items = items,
            currentRoute = items[currentRoute].route,
            onItemClick = { currentRoute = items.indexOf(it) }
        )
    }
}
