package com.dilara.beatify.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dilara.beatify.presentation.ui.navigation.model.BottomNavItem
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightPrimary
import com.dilara.beatify.ui.theme.LightSecondary
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.LightTertiary
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonPink
import com.dilara.beatify.ui.theme.NeonTextSecondary
import com.dilara.beatify.ui.theme.isDarkTheme

@Composable
fun BeatifyBottomNavigationBar(
    items: List<BottomNavItem>,
    selectedRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val systemBars = WindowInsets.systemBars
    val density = androidx.compose.ui.platform.LocalDensity.current
    val bottomPadding = with(density) { systemBars.getBottom(density).toDp() }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp + bottomPadding)
            .padding(bottom = bottomPadding)
            .background(
                brush = if (isDarkTheme) {
                    Brush.verticalGradient(
                        colors = listOf(
                            DarkSurface.copy(alpha = 0.85f),
                            DarkSurface.copy(alpha = 0.95f)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            LightSurface.copy(alpha = 0.98f),
                            LightSurface.copy(alpha = 1f)
                        )
                    )
                }
            )
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (isDarkTheme) {
                        Brush.horizontalGradient(
                            colors = listOf(
                                NeonPurple.copy(alpha = 0.1f),
                                NeonCyan.copy(alpha = 0.05f),
                                NeonPink.copy(alpha = 0.1f)
                            )
                        )
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(
                                LightPrimary.copy(alpha = 0.08f),
                                LightTertiary.copy(alpha = 0.06f),
                                LightSecondary.copy(alpha = 0.05f)
                            )
                        )
                    }
                )
        )
        
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                BottomNavBarItem(
                    item = item,
                    isSelected = selectedRoute == item.route,
                    onClick = { onItemClick(item.route) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavBarItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(300),
        label = "scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.6f,
        animationSpec = tween(300),
        label = "alpha"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) {
                    NeonCyan.copy(alpha = 0.15f)
                } else if (isPressed) {
                    NeonTextSecondary.copy(alpha = 0.1f)
                } else {
                    Color.Transparent
                },
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .scale(scale)
                .alpha(alpha),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    NeonCyan.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
            }
            
            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.icon,
                contentDescription = item.label,
                tint = if (isSelected) {
                    NeonCyan
                } else {
                    NeonTextSecondary
                },
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

