package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.unit.dp
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightPrimary
import com.dilara.beatify.ui.theme.LightSecondary
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.LightTertiary
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.isDarkTheme
import com.dilara.beatify.core.utils.stringResource
import com.dilara.beatify.R

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 64.dp
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(200),
        label = "fab_scale"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size * 2.2f)
                .background(
                    brush = if (isDarkTheme) {
                        Brush.radialGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.4f),
                                NeonCyan.copy(alpha = 0.25f),
                                NeonCyan.copy(alpha = 0.15f),
                                NeonCyan.copy(alpha = 0.08f),
                                Transparent
                            ),
                            radius = size.value * 1.2f
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                LightPrimary.copy(alpha = 0.35f),
                                LightTertiary.copy(alpha = 0.25f),
                                LightSecondary.copy(alpha = 0.15f),
                                Transparent
                            ),
                            radius = size.value * 1.2f
                        )
                    },
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    brush = if (isDarkTheme) {
                        Brush.linearGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.6f),
                                NeonPurple.copy(alpha = 0.5f),
                                NeonCyan.copy(alpha = 0.6f)
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                LightPrimary.copy(alpha = 0.8f),
                                LightTertiary.copy(alpha = 0.7f),
                                LightSecondary.copy(alpha = 0.6f)
                            )
                        )
                    }
                )
                .padding(1.5.dp)
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
                .clickable(onClick = onClick)
                .scale(scale),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        brush = if (isDarkTheme) {
                            Brush.radialGradient(
                                colors = listOf(
                                    NeonCyan.copy(alpha = 0.2f),
                                    NeonPurple.copy(alpha = 0.15f),
                                    Transparent
                                ),
                                radius = size.value * 0.7f
                            )
                        } else {
                            Brush.radialGradient(
                                colors = listOf(
                                    LightPrimary.copy(alpha = 0.15f),
                                    LightTertiary.copy(alpha = 0.1f),
                                    Transparent
                                ),
                                radius = size.value * 0.7f
                            )
                        }
                    )
            )
            
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.cd_create_playlist),
                tint = if (isDarkTheme) Color(0xFFE0E0E0) else LightPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

