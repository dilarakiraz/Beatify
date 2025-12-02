package com.dilara.beatify.presentation.ui.components.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.dilara.beatify.ui.theme.BeatifyColors
import com.dilara.beatify.ui.theme.BeatifyGradients
import com.dilara.beatify.ui.theme.LightPrimary

/**
 * Reusable drawer menu item with modern design
 */
@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    isDarkTheme: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(100),
        label = "scale",
        finishedListener = { isPressed = false }
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    isPressed = true
                    onClick()
                }
            )
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BeatifyGradients.iconContainerGradient(isDarkTheme)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = BeatifyColors.accentColor(isDarkTheme),
                    modifier = Modifier.size(16.dp)
                )
            }

            trailing?.invoke()
        }
    }
}

/**
 * Theme switcher menu item with switch
 */
@Composable
fun ThemeSwitcherMenuItem(
    isDarkTheme: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    DrawerMenuItem(
        icon = if (isDarkTheme) {
            androidx.compose.material.icons.Icons.Default.DarkMode
        } else {
            androidx.compose.material.icons.Icons.Default.LightMode
        },
        title = if (isDarkTheme) "Koyu Tema" else "Açık Tema",
        isDarkTheme = isDarkTheme,
        onClick = onToggle,
        modifier = modifier,
        trailing = {
            androidx.compose.material3.Switch(
                checked = isDarkTheme,
                onCheckedChange = { onToggle() },
                modifier = Modifier.scale(0.75f),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = if (isDarkTheme) {
                        com.dilara.beatify.ui.theme.NeonCyan
                    } else {
                        androidx.compose.ui.graphics.Color.White
                    },
                    checkedTrackColor = if (isDarkTheme) {
                        com.dilara.beatify.ui.theme.NeonCyan.copy(alpha = 0.4f)
                    } else {
                        LightPrimary
                    },
                    uncheckedThumbColor = if (isDarkTheme) {
                        com.dilara.beatify.ui.theme.NeonTextSecondary
                    } else {
                        com.dilara.beatify.ui.theme.LightTextSecondary
                    },
                    uncheckedTrackColor = if (isDarkTheme) {
                        com.dilara.beatify.ui.theme.NeonTextSecondary.copy(alpha = 0.3f)
                    } else {
                        com.dilara.beatify.ui.theme.LightTextSecondary.copy(alpha = 0.35f)
                    }
                )
            )
        }
    )
}

