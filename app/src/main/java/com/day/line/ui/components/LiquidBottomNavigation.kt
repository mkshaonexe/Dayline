package com.day.line.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.day.line.ui.theme.GlassBlack
import com.day.line.ui.theme.DaylineOrange

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object Journaling : BottomNavItem("Journal", Icons.Default.Edit, "journaling")
    object Dayline : BottomNavItem("Dayline", Icons.Default.ViewStream, "dayline")
    object Profile : BottomNavItem("Profile", Icons.Default.Person, "profile")
    object Settings : BottomNavItem("Settings", Icons.Default.Settings, "settings")
}

@Composable
fun LiquidBottomNavigation(
    items: List<BottomNavItem>,
    currentRoute: String,
    onItemClick: (BottomNavItem) -> Unit
) {
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }.takeIf { it != -1 } ?: 0

    // Determine glass colors based on theme
    // We can rely on MaterialTheme.colorScheme.background luminance or simple state
    val isDark = androidx.compose.foundation.isSystemInDarkTheme() // Or pass it in. For now, system theme check.
    // However, since we force theme in Main, this composable needs to know about it.
    // The easiest way is to check background color brightness or assume if surface is dark.
    // Let's use MaterialTheme.colorScheme.surface's luminance logic implicitly by using tokens, 
    // BUT we want glass.
    
    // Better: Check if primary container is dark?
    // Let's check a known dark color token, e.g. background
    // Color.luminance requires api 26 for Color class in some contexts, or we can just assume standard.
    // Safest: Use MaterialTheme.colorScheme.background == Color(0xFF1C1B1F) (approx).
    
    // Actually, simpler: Use a theme aware variable.
    val glassBackgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    val glassBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    val unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    val shadowColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.2f)

    Box(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 12.dp) // Provide floating margins
            .fillMaxWidth()
            .height(64.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(50),
                spotColor = shadowColor,
                ambientColor = shadowColor
            )
            .clip(RoundedCornerShape(50)) // Full pill shape
            .background(color = glassBackgroundColor)
            .border(
                width = 1.dp,
                color = glassBorderColor,
                shape = RoundedCornerShape(50)
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
                                            DaylineOrange, // Primary Orange
                                            Color(0xFFFFB74D).copy(alpha = 0.6f)  // Lighter Orange shine
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
                            tint = unselectedIconColor, 
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
        BottomNavItem.Journaling,
        BottomNavItem.Dayline,
        BottomNavItem.Profile,
        BottomNavItem.Settings
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
