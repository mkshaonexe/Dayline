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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewStream
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

// Apple Liquid Glass Colors
private val LiquidGlassLight = Color(0xFFE8EAED)
private val LiquidGlassDark = Color(0xFF2A2D32)
private val LiquidGlassHighlight = Color(0xFFFFFFFF)
private val LiquidGlassRefraction = Color(0xFFE0F7FA) // Subtle cyan tint for light refraction
private val LiquidGlassRainbow = Color(0xFFFFE4B5) // Subtle rainbow edge effect

@Composable
fun LiquidBottomNavigation(
    items: List<BottomNavItem>,
    currentRoute: String,
    onItemClick: (BottomNavItem) -> Unit
) {
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }.takeIf { it != -1 } ?: 0
    val isDark = isSystemInDarkTheme()

    // Apple Liquid Glass: Frosted translucent base
    val liquidGlassBase = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF2A2D32).copy(alpha = 0.90f),
                Color(0xFF1F2226).copy(alpha = 0.95f),
                Color(0xFF2A2D32).copy(alpha = 0.90f)
            ),
            start = Offset(0f, 0f),
            end = Offset(1000f, 60f)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFF5F7FA).copy(alpha = 0.25f),
                Color(0xFFE8EAED).copy(alpha = 0.15f),
                Color(0xFFF0F2F5).copy(alpha = 0.20f)
            ),
            start = Offset(0f, 0f),
            end = Offset(1000f, 60f)
        )
    }

    // Top highlight for 3D glass effect
    val topHighlight = Brush.verticalGradient(
        colors = listOf(
            if (isDark) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.8f),
            Color.Transparent
        ),
        startY = 0f,
        endY = 25f
    )

    // Inner shadow effect
    val innerShadow = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            if (isDark) Color.Black.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.05f)
        ),
        startY = 40f,
        endY = 60f
    )

    // Border: Subtle glass rim with refraction
    val glassBorder = Brush.linearGradient(
        colors = if (isDark) listOf(
            Color.White.copy(alpha = 0.30f),
            Color(0xFF80DEEA).copy(alpha = 0.15f), // Subtle cyan refraction
            Color.White.copy(alpha = 0.10f),
            Color(0xFFCE93D8).copy(alpha = 0.12f), // Subtle purple refraction
            Color.White.copy(alpha = 0.25f)
        ) else listOf(
            Color.White.copy(alpha = 0.9f),
            Color(0xFF80DEEA).copy(alpha = 0.25f),
            Color.White.copy(alpha = 0.5f),
            Color(0xFFCE93D8).copy(alpha = 0.20f),
            Color.White.copy(alpha = 0.8f)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 60f)
    )

    val shadowColor = if (isDark) Color.Black.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.12f)
    val unselectedIconColor = if (isDark) {
        Color.White.copy(alpha = 0.5f)
    } else {
        Color(0xFF4A4A4A).copy(alpha = 0.6f)
    }

    Box(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 20.dp)
            .fillMaxWidth()
            .height(56.dp)
            // Outer glow for floating effect
            .shadow(
                elevation = 32.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = shadowColor,
                ambientColor = shadowColor.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(28.dp))
            // Base glass layer
            .background(brush = liquidGlassBase)
            // Glass border with refraction
            .border(
                width = 1.5.dp,
                brush = glassBorder,
                shape = RoundedCornerShape(28.dp)
            )
            // Add static highlight overlay
            .drawWithContent {
                drawContent()
                // Top highlight
                drawRect(brush = topHighlight)
                // Inner shadow at bottom
                drawRect(brush = innerShadow)
            }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                // Organic liquid spring animation
                val weight by animateFloatAsState(
                    targetValue = if (isSelected) 1.6f else 1f,
                    animationSpec = spring(
                        dampingRatio = 0.6f, // More fluid/organic feel
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "weight"
                )

                // Scale animation for morphing effect
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.05f else 1f,
                    animationSpec = spring(
                        dampingRatio = 0.5f,
                        stiffness = Spring.StiffnessMediumLow
                    ),
                    label = "scale"
                )

                // Selected item glow/highlight gradient
                val selectedGradient = if (isDark) {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF4A4D52).copy(alpha = 0.9f),
                            Color(0xFF3A3D42).copy(alpha = 0.8f)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.95f),
                            Color(0xFFF5F7FA).copy(alpha = 0.85f)
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(weight)
                        .fillMaxHeight()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .padding(horizontal = 2.dp)
                        .then(
                            if (isSelected) {
                                Modifier
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(20.dp),
                                        spotColor = if (isDark) Color.Black.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.1f)
                                    )
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(brush = selectedGradient)
                                    .border(
                                        width = 1.dp,
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.White.copy(alpha = if (isDark) 0.2f else 0.9f),
                                                Color.White.copy(alpha = if (isDark) 0.05f else 0.3f)
                                            )
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            } else {
                                Modifier.clip(RoundedCornerShape(20.dp))
                            }
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onItemClick(item) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isDark) Color.White else Color(0xFF1A1A1A),
                                modifier = Modifier.size(18.dp)
                            )

                            Text(
                                text = item.title,
                                color = if (isDark) Color.White else Color(0xFF1A1A1A),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                letterSpacing = 0.3.sp,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = unselectedIconColor,
                            modifier = Modifier.size(22.dp)
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
