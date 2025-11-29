package com.dilara.beatify.presentation.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.NeonTextSecondary

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = if (isSecondary) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            DarkSurface.copy(alpha = 0.6f),
                            DarkSurface.copy(alpha = 0.4f)
                        )
                    )
                } else if (enabled) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            NeonCyan.copy(alpha = 0.35f),
                            NeonPurple.copy(alpha = 0.3f)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            NeonTextSecondary.copy(alpha = 0.3f),
                            NeonTextSecondary.copy(alpha = 0.2f)
                        )
                    )
                }
            )
            .then(
                if (enabled || isSecondary) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSecondary) {
                NeonTextSecondary
            } else if (enabled) {
                NeonTextPrimary
            } else {
                NeonTextSecondary.copy(alpha = 0.5f)
            }
        )
    }
}
