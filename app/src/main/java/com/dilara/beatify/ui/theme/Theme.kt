package com.dilara.beatify.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NeonDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = NeonCyan.copy(alpha = 0.2f),
    onPrimaryContainer = NeonCyanLight,

    secondary = NeonPink,
    onSecondary = Color.Black,
    secondaryContainer = NeonPink.copy(alpha = 0.2f),
    onSecondaryContainer = NeonPinkLight,

    tertiary = NeonPurple,
    onTertiary = Color.White,
    tertiaryContainer = NeonPurple.copy(alpha = 0.2f),
    onTertiaryContainer = NeonPurpleLight,

    error = Color(0xFFE57373),
    onError = Color.Black,
    errorContainer = Color(0xFFE57373).copy(alpha = 0.2f),
    onErrorContainer = Color(0xFFFFB4A2),

    background = DarkBackground,
    onBackground = NeonTextPrimary,

    surface = DarkSurface,
    onSurface = NeonTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = NeonTextSecondary,

    outline = NeonPurple.copy(alpha = 0.5f),
    outlineVariant = NeonCyan.copy(alpha = 0.3f),

    inverseSurface = NeonTextPrimary,
    inverseOnSurface = DarkBackground,
    inversePrimary = NeonCyan
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = LightPrimaryLight.copy(alpha = 0.3f),
    onPrimaryContainer = LightPrimary,

    secondary = LightSecondary,
    onSecondary = Color.White,
    secondaryContainer = LightSecondaryLight.copy(alpha = 0.3f),
    onSecondaryContainer = LightSecondary,

    tertiary = LightTertiary,
    onTertiary = Color.White,
    tertiaryContainer = LightTertiaryLight.copy(alpha = 0.3f),
    onTertiaryContainer = LightTertiary,

    error = Color(0xFFD32F2F),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),

    background = LightBackground,
    onBackground = LightTextPrimary,

    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,

    outline = LightTextSecondary.copy(alpha = 0.5f),
    outlineVariant = LightTextSecondary.copy(alpha = 0.3f),

    inverseSurface = LightTextPrimary,
    inverseOnSurface = LightBackground,
    inversePrimary = LightPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)


@Composable
fun BeatifyTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) NeonDarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val backgroundColor = if (darkTheme) DarkBackground else LightBackground
            window.statusBarColor = backgroundColor.toArgb()
            window.navigationBarColor = backgroundColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    androidx.compose.runtime.CompositionLocalProvider(LocalIsDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}