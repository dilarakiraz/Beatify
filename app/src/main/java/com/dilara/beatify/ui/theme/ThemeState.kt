package com.dilara.beatify.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

/**
 * Tema durumu için local composition
 * Tüm composable'ların kolay tema kontrolü yapmasını sağlar
 */
val LocalIsDarkTheme = compositionLocalOf { true }

/**
 * Koyu tema aktif mi kontrol eder
 * Kullanım: if (isDarkTheme) { ... }
 */
val isDarkTheme: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalIsDarkTheme.current

/**
 * Tema-aware surface rengi
 * Otomatik olarak DarkSurface veya LightSurface döndürür
 */
val themeSurface: androidx.compose.ui.graphics.Color
    @Composable
    @ReadOnlyComposable
    get() = if (isDarkTheme) DarkSurface else LightSurface

/**
 * Tema-aware arka plan rengi
 * Otomatik olarak DarkBackground veya LightBackground döndürür
 */
val themeBackground: androidx.compose.ui.graphics.Color
    @Composable
    @ReadOnlyComposable
    get() = if (isDarkTheme) DarkBackground else LightBackground

/**
 * Tema-aware birincil text rengi
 */
val themeTextPrimary: androidx.compose.ui.graphics.Color
    @Composable
    @ReadOnlyComposable
    get() = if (isDarkTheme) NeonTextPrimary else LightTextPrimary

/**
 * Tema-aware ikincil text rengi
 */
val themeTextSecondary: androidx.compose.ui.graphics.Color
    @Composable
    @ReadOnlyComposable
    get() = if (isDarkTheme) NeonTextSecondary else LightTextSecondary

/**
 * Tema-aware accent rengi
 */
val themeAccent: androidx.compose.ui.graphics.Color
    @Composable
    @ReadOnlyComposable
    get() = if (isDarkTheme) NeonCyan else LightPrimary

