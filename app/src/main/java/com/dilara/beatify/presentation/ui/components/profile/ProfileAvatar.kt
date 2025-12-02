package com.dilara.beatify.presentation.ui.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dilara.beatify.ui.theme.BeatifyGradients
import com.dilara.beatify.ui.theme.LightPrimary
import com.dilara.beatify.ui.theme.LightTextSecondary
import com.dilara.beatify.ui.theme.NeonTextSecondary
import com.dilara.beatify.ui.theme.isDarkTheme

/**
 * Ortak profil avatar component'i
 * Glass morphism efekti ile
 */
@Composable
fun ProfileAvatar(
    profileImageUri: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    glowSize: Dp = 60.dp,
    iconSize: Dp = 28.dp
) {
    Box(
        modifier = modifier.size(glowSize),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(brush = BeatifyGradients.radialGlow(isDarkTheme))
        )
        
        // Glass container
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    brush = if (isDarkTheme) {
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.05f),
                                Color.White.copy(alpha = 0.02f)
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.95f),
                                LightPrimary.copy(alpha = 0.08f)
                            )
                        )
                    }
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = if (isDarkTheme) {
                        NeonTextSecondary.copy(alpha = 0.6f)
                    } else {
                        LightTextSecondary.copy(alpha = 0.7f)
                    },
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}

