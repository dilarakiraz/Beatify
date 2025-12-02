package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dilara.beatify.ui.theme.LightPrimary
import com.dilara.beatify.ui.theme.LightSecondary
import com.dilara.beatify.ui.theme.LightTertiary
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple

/**
 * Glass morphism container with glow effect
 * Modern, elegant container for profile images, icons, etc.
 */
@Composable
fun GlassContainer(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    glowSize: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Outer glow effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(
                    brush = if (isDarkTheme) {
                        Brush.radialGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.3f),
                                NeonPurple.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                LightPrimary.copy(alpha = 0.35f),
                                LightTertiary.copy(alpha = 0.25f),
                                LightSecondary.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    }
                )
        )
        
        // Glass morphism inner container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .then(
                    if (isDarkTheme) {
                        Modifier.background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.05f),
                                    Color.White.copy(alpha = 0.02f)
                                )
                            )
                        )
                    } else {
                        Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.95f),
                                        LightPrimary.copy(alpha = 0.08f),
                                        LightSecondary.copy(alpha = 0.05f)
                                    )
                                )
                            )
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        LightPrimary.copy(alpha = 0.3f),
                                        LightTertiary.copy(alpha = 0.2f)
                                    )
                                ),
                                shape = shape
                            )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

/**
 * Glass card container with rounded corners
 */
@Composable
fun GlassCard(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    glowSize: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    GlassContainer(
        isDarkTheme = isDarkTheme,
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        glowSize = glowSize,
        content = content
    )
}

