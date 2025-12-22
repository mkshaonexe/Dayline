package com.day.line.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private fun getDarkColorScheme(accentColor: Color) = darkColorScheme(
    primary = accentColor,
    secondary = Secondary,
    tertiary = Tertiary,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = TextWhite,
    onSurface = TextWhite,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = TextGrey,
    outline = GlassBorderDark,
    outlineVariant = GlassBorderDark.copy(alpha = 0.3f)
)

private fun getLightColorScheme(accentColor: Color) = lightColorScheme(
    primary = accentColor,
    secondary = Secondary,
    tertiary = Tertiary,
    background = OffWhite,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextBlack,
    onSurface = TextBlack,
    surfaceVariant = PastelGrey,
    onSurfaceVariant = TextGrey,
    outline = GlassBorderLight,
    outlineVariant = GlassBorderLight.copy(alpha = 0.5f)
)

@Composable
fun DaylineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accentColor: Color = Color(0xFFFF8A00), // Default Orange
    dynamicColor: Boolean = false, // Disable dynamic color to enforce brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> getDarkColorScheme(accentColor)
        else -> getLightColorScheme(accentColor)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // If we are in dark theme (app setting), we want light icons (so isAppearanceLightStatusBars = false)
            // If we are in light theme, we want dark icons (so isAppearanceLightStatusBars = true)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalThemeColor provides accentColor,
        LocalDarkTheme provides darkTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val LocalDarkTheme = androidx.compose.runtime.staticCompositionLocalOf { false }