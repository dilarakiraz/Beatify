package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightPrimary
import com.dilara.beatify.ui.theme.LightSecondary
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.LightTertiary
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPink
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.isDarkTheme

enum class GlassIconButtonStyle {
    PRIMARY, // Cyan-Purple (BackButton style)
    DANGER   // Pink-Purple (DeleteButton style)
}

@Composable
fun GlassIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GlassIconButtonStyle = GlassIconButtonStyle.PRIMARY,
    size: Dp = 48.dp,
    iconSize: Dp = 24.dp,
    iconTint: Color = NeonTextPrimary,
    contentDescription: String? = null
) {
    val gradientColors = if (isDarkTheme) {
        when (style) {
            GlassIconButtonStyle.PRIMARY -> listOf(
                NeonCyan.copy(alpha = 0.7f),
                NeonPurple.copy(alpha = 0.6f),
                NeonCyan.copy(alpha = 0.7f)
            )
            GlassIconButtonStyle.DANGER -> listOf(
                NeonPink.copy(alpha = 0.7f),
                NeonPurple.copy(alpha = 0.6f),
                NeonPink.copy(alpha = 0.7f)
            )
        }
    } else {
        when (style) {
            GlassIconButtonStyle.PRIMARY -> listOf(
                LightPrimary.copy(alpha = 0.8f),
                LightTertiary.copy(alpha = 0.7f),
                LightPrimary.copy(alpha = 0.8f)
            )
            GlassIconButtonStyle.DANGER -> listOf(
                LightSecondary.copy(alpha = 0.8f),
                LightPrimary.copy(alpha = 0.7f),
                LightSecondary.copy(alpha = 0.8f)
            )
        }
    }
    
    val glowColors = if (isDarkTheme) {
        when (style) {
            GlassIconButtonStyle.PRIMARY -> listOf(
                NeonCyan.copy(alpha = 0.2f),
                NeonPurple.copy(alpha = 0.15f),
                Color.Transparent
            )
            GlassIconButtonStyle.DANGER -> listOf(
                NeonPink.copy(alpha = 0.2f),
                NeonPink.copy(alpha = 0.15f),
                Color.Transparent
            )
        }
    } else {
        when (style) {
            GlassIconButtonStyle.PRIMARY -> listOf(
                LightPrimary.copy(alpha = 0.3f),
                LightTertiary.copy(alpha = 0.2f),
                Color.Transparent
            )
            GlassIconButtonStyle.DANGER -> listOf(
                LightSecondary.copy(alpha = 0.35f),
                LightSecondary.copy(alpha = 0.2f),
                Color.Transparent
            )
        }
    }
    
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(colors = gradientColors)
            )
            .padding(2.dp)
            .background(
                brush = if (isDarkTheme) {
                    Brush.radialGradient(
                        colors = listOf(
                            DarkSurface.copy(alpha = 0.9f),
                            DarkSurface.copy(alpha = 0.75f)
                        )
                    )
                } else {
                    Brush.radialGradient(
                        colors = listOf(
                            LightSurface.copy(alpha = 0.95f),
                            LightSurface.copy(alpha = 0.85f)
                        )
                    )
                },
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = glowColors,
                        radius = size.value * 0.7f
                    )
                )
        )
        
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isDarkTheme) {
                when (style) {
                    GlassIconButtonStyle.PRIMARY -> iconTint
                    GlassIconButtonStyle.DANGER -> NeonPink.copy(alpha = 0.9f)
                }
            } else {
                when (style) {
                    GlassIconButtonStyle.PRIMARY -> LightPrimary
                    GlassIconButtonStyle.DANGER -> LightSecondary
                }
            },
            modifier = Modifier.size(iconSize)
        )
    }
}

