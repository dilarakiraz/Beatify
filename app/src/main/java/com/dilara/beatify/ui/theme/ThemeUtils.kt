package com.dilara.beatify.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Merkezi tema-aware gradient ve renk yardımcıları
 */
@Stable
object BeatifyGradients {
    
    // İkon container gradient'leri
    fun iconContainerGradient(isDarkTheme: Boolean): Brush {
        return if (isDarkTheme) {
            Brush.linearGradient(
                colors = listOf(
                    NeonCyan.copy(alpha = 0.3f),
                    NeonPurple.copy(alpha = 0.2f)
                )
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    LightPrimary.copy(alpha = 0.18f),
                    LightTertiary.copy(alpha = 0.15f)
                )
            )
        }
    }
    
    // Radial glow gradient (ProfileAvatar, pulse efektler)
    fun radialGlow(isDarkTheme: Boolean, alpha: Float = 1f): Brush {
        return Brush.radialGradient(
            colors = if (isDarkTheme) {
                listOf(
                    NeonCyan.copy(alpha = 0.3f * alpha),
                    NeonPurple.copy(alpha = 0.2f * alpha),
                    Color.Transparent
                )
            } else {
                listOf(
                    LightPrimary.copy(alpha = 0.35f * alpha),
                    LightTertiary.copy(alpha = 0.25f * alpha),
                    Color.Transparent
                )
            }
        )
    }
    
    // Horizontal card gradient
    fun cardGradientHorizontal(isDarkTheme: Boolean): Brush {
        return Brush.horizontalGradient(
            colors = if (isDarkTheme) {
                listOf(
                    NeonPurple.copy(alpha = 0.05f),
                    Color.Transparent,
                    NeonCyan.copy(alpha = 0.05f)
                )
            } else {
                listOf(
                    LightPrimary.copy(alpha = 0.08f),
                    LightTertiary.copy(alpha = 0.06f),
                    Color.Transparent
                )
            }
        )
    }
    
    // Linear card gradient (daha belirgin)
    fun cardGradientLinear(isDarkTheme: Boolean): Brush {
        return Brush.linearGradient(
            colors = if (isDarkTheme) {
                listOf(
                    NeonPurple.copy(alpha = 0.1f),
                    Color.Transparent,
                    NeonCyan.copy(alpha = 0.1f)
                )
            } else {
                listOf(
                    LightPrimary.copy(alpha = 0.12f),
                    LightTertiary.copy(alpha = 0.08f),
                    LightSecondary.copy(alpha = 0.06f)
                )
            }
        )
    }
    
    // Mini player gradient (3 renk)
    fun miniPlayerGradient(isDarkTheme: Boolean): Brush {
        return Brush.horizontalGradient(
            colors = if (isDarkTheme) {
                listOf(
                    NeonPurple.copy(alpha = 0.15f),
                    NeonCyan.copy(alpha = 0.1f),
                    NeonPink.copy(alpha = 0.15f)
                )
            } else {
                listOf(
                    LightPrimary.copy(alpha = 0.12f),
                    LightTertiary.copy(alpha = 0.09f),
                    LightSecondary.copy(alpha = 0.08f)
                )
            }
        )
    }
    
}

/**
 * Merkezi tema-aware renkler
 * Not: Çoğu durumda ThemeState.kt'deki extension'ları kullan (isDarkTheme, themeBackground vb.)
 */
@Stable
object BeatifyColors {
    fun accentColor(isDarkTheme: Boolean): Color {
        return if (isDarkTheme) NeonCyan else LightPrimary
    }
}


