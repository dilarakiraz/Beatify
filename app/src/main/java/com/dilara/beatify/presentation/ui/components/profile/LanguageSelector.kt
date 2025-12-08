package com.dilara.beatify.presentation.ui.components.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dilara.beatify.ui.theme.LightPrimary
import com.dilara.beatify.ui.theme.LightSecondary
import com.dilara.beatify.ui.theme.LightTextPrimary
import com.dilara.beatify.ui.theme.LightTextSecondary
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.NeonTextSecondary
import com.dilara.beatify.ui.theme.isDarkTheme

enum class AppLanguage {
    TR, EN
}

@Composable
fun LanguageSelector(
    selectedLanguage: AppLanguage = AppLanguage.TR,
    onLanguageChange: (AppLanguage) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LanguageOption(
            language = AppLanguage.TR,
            isSelected = selectedLanguage == AppLanguage.TR,
            onClick = { onLanguageChange(AppLanguage.TR) },
            modifier = Modifier.weight(1f)
        )

        LanguageOption(
            language = AppLanguage.EN,
            isSelected = selectedLanguage == AppLanguage.EN,
            onClick = { onLanguageChange(AppLanguage.EN) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LanguageOption(
    language: AppLanguage,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "scale",
        finishedListener = { isPressed = false }
    )

    val isDark = isDarkTheme

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = {
                isPressed = true
                onClick()
            })
            .background(
                if (isSelected) {
                    if (isDark) {
                        Brush.horizontalGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.4f),
                                NeonPurple.copy(alpha = 0.3f)
                            )
                        )
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(
                                LightPrimary.copy(alpha = 0.6f),
                                LightSecondary.copy(alpha = 0.4f)
                            )
                        )
                    }
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                }
            )
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = language.name,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) {
                if (isDark) NeonTextPrimary else LightTextPrimary
            } else {
                if (isDark) NeonTextSecondary.copy(alpha = 0.7f) else LightTextSecondary.copy(alpha = 0.7f)
            }
        )
    }
}

