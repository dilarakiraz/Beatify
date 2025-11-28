package com.dilara.beatify.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Neon Dark Color Scheme - Wild Roots Brewery Inspired
private val NeonDarkColorScheme = darkColorScheme(
    primary = NeonCyan,                    // Main accent - cyan glow
    onPrimary = Color.Black,
    primaryContainer = NeonCyan.copy(alpha = 0.2f),
    onPrimaryContainer = NeonCyanLight,
    
    secondary = NeonPink,                  // Secondary accent - pink glow
    onSecondary = Color.Black,
    secondaryContainer = NeonPink.copy(alpha = 0.2f),
    onSecondaryContainer = NeonPinkLight,
    
    tertiary = NeonPurple,                 // Tertiary accent - purple glow
    onTertiary = Color.White,
    tertiaryContainer = NeonPurple.copy(alpha = 0.2f),
    onTertiaryContainer = NeonPurpleLight,
    
    error = NeonPink,
    onError = Color.Black,
    errorContainer = NeonPink.copy(alpha = 0.2f),
    onErrorContainer = NeonPinkLight,
    
    background = DarkBackground,           // Very dark background
    onBackground = NeonTextPrimary,
    
    surface = DarkSurface,                 // Dark surface
    onSurface = NeonTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = NeonTextSecondary,
    
    outline = NeonPurple.copy(alpha = 0.5f),
    outlineVariant = NeonCyan.copy(alpha = 0.3f),
    
    inverseSurface = NeonTextPrimary,
    inverseOnSurface = DarkBackground,
    inversePrimary = NeonCyan
)

// Legacy color schemes (kept for backward compatibility)
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun BeatifyTheme(
    darkTheme: Boolean = true, // Default to dark theme for neon aesthetic
    // Dynamic color disabled for custom neon theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Always use neon dark theme for now (can add light variant later if needed)
        darkTheme -> NeonDarkColorScheme
        else -> NeonDarkColorScheme // Use dark even in light mode for neon effect
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkBackground.toArgb()
            window.navigationBarColor = DarkBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}