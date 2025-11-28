package com.dilara.beatify.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dilara.beatify.presentation.ui.navigation.model.BottomNavItem
import com.dilara.beatify.ui.theme.*

@Composable
fun BeatifyBottomNavigationBar(
    items: List<BottomNavItem>,
    selectedRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkSurface.copy(alpha = 0.85f),
                        DarkSurface.copy(alpha = 0.95f)
                    )
                )
            )
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = 0.1f),
                            NeonCyan.copy(alpha = 0.05f),
                            NeonPink.copy(alpha = 0.1f)
                        )
                    )
                )
        )
        
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 4.dp),
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
            .fillMaxHeight()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) {
                    getNeonColorForRoute(item.route).copy(alpha = 0.15f)
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
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
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
                                        getNeonColorForRoute(item.route).copy(alpha = 0.3f),
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
                        getNeonColorForRoute(item.route)
                    } else {
                        NeonTextSecondary
                    },
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(
                text = item.label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) {
                    getNeonColorForRoute(item.route)
                } else {
                    NeonTextSecondary
                },
                maxLines = 1
            )
        }
    }
}

@Composable
private fun getNeonColorForRoute(route: String): Color {
    return when (route) {
        "home" -> NeonCyan
        "search" -> NeonPink
        "favorites" -> NeonOrange
        "playlists" -> NeonPurple
        "profile" -> NeonGreen
        else -> NeonCyan
    }
}


