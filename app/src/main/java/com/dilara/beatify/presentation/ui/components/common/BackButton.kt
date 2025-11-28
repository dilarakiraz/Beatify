package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.unit.dp
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonTextPrimary

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = 0.7f),
                        NeonPurple.copy(alpha = 0.6f),
                        NeonCyan.copy(alpha = 0.7f)
                    )
                )
            )
            .padding(2.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        DarkSurface.copy(alpha = 0.9f),
                        DarkSurface.copy(alpha = 0.75f)
                    )
                ),
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
                        colors = listOf(
                            NeonCyan.copy(alpha = 0.2f),
                            NeonPurple.copy(alpha = 0.15f),
                            Transparent
                        ),
                        radius = 48f * 0.7f
                    )
                )
        )
        
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Geri",
            tint = NeonTextPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}

