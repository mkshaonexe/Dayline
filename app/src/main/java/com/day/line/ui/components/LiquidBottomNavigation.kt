package com.day.line.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

import com.day.line.ui.theme.DaylineOrange
import com.day.line.ui.theme.NeonOrange
import com.day.line.ui.theme.ElectricBlue
import com.day.line.ui.theme.GlassBlack
import com.day.line.ui.theme.GlassWhite
import com.day.line.ui.theme.GlassBorderLight
import com.day.line.ui.theme.GlassBorderDark

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
    val isDark = isSystemInDarkTheme()

    // Premium Glass Effect: Directional Gradient for realistic lighting
    val glassGradient = Brush.linearGradient(
        colors = if (isDark) listOf(
            Color(0xFF252525).copy(alpha = 0.9f), // Top-Left Light
            Color(0xFF151515).copy(alpha = 0.8f)  // Bottom-Right Shadow
        ) else listOf(
            Color(0xFFFFFFFF).copy(alpha = 0.95f),
            Color(0xFFF2F2F2).copy(alpha = 0.85f)
        ),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(1000f, 100f) // Angled light
    )

    // Border: Subtle rim light
    val borderBrush = Brush.verticalGradient(
        colors = if (isDark) listOf(
            GlassBorderDark.copy(alpha = 0.2f),
            GlassBorderDark.copy(alpha = 0.05f)
        ) else listOf(
            GlassBorderLight.copy(alpha = 0.6f),
            GlassBorderLight.copy(alpha = 0.1f)
        )
    )

    val shadowColor = if (isDark) Color.Black.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.15f)
    val unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) // Faded for hierarchy

    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp) // Generous float
            .fillMaxWidth()
            .height(60.dp) // Compact, premium feel
            .shadow(
                elevation = 24.dp, // High elevation for "float"
                shape = RoundedCornerShape(100.dp),
                spotColor = shadowColor,
                ambientColor = shadowColor
            )
            .clip(RoundedCornerShape(100.dp))
            .background(brush = glassGradient)
            .border(
                width = 1.dp,
                brush = borderBrush,
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                
                // Spring Animation for "Fluid" feel
                val weight by animateFloatAsState(
                    targetValue = if (isSelected) 1.8f else 1f, // More expansion
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "weight"
                )

                Box(
                    modifier = Modifier
                        .weight(weight)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(100.dp)) // Fully rounded internal pills
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onItemClick(item) }
                        .then(
                            if (isSelected) {
                                Modifier.background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            DaylineOrange,     // Core
                                            NeonOrange         // Shine
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Box(modifier = Modifier.padding(start = 10.dp)) {
                                Text(
                                    text = item.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.5.sp // Clean typography
                                )
                            }
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = unselectedIconColor, 
                            modifier = Modifier.size(24.dp)
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
    
    Box(modifier = Modifier.background(Color(0xFF141414)).padding(40.dp)) {
        LiquidBottomNavigation(
            items = items,
            currentRoute = items[currentRoute].route,
            onItemClick = { currentRoute = items.indexOf(it) }
        )
    }
}
