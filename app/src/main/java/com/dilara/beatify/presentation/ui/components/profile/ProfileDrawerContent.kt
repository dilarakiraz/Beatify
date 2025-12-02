package com.dilara.beatify.presentation.ui.components.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.presentation.ui.components.common.GlassCard
import com.dilara.beatify.presentation.viewmodel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDrawerContent(
    profileImageUri: String?,
    onDismiss: () -> Unit,
    themeViewModel: ThemeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val isThemeDark by themeViewModel.isDarkTheme.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (isThemeDark) {
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            com.dilara.beatify.ui.theme.DarkBackground,
                            com.dilara.beatify.ui.theme.DarkSurface,
                            com.dilara.beatify.ui.theme.DarkSurfaceVariant
                        )
                    )
                } else {
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            com.dilara.beatify.ui.theme.LightBackground,
                            com.dilara.beatify.ui.theme.LightSurface,
                            com.dilara.beatify.ui.theme.LightSurfaceVariant.copy(alpha = 0.5f)
                        )
                    )
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(400)) + slideInVertically(
                    initialOffsetY = { -it / 2 },
                    animationSpec = tween(400)
                )
            ) {
                ProfileAvatar(
                    profileImageUri = profileImageUri,
                    onClick = onDismiss,
                    size = 64.dp,
                    glowSize = 80.dp,
                    iconSize = 36.dp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(20.dp))


            Spacer(modifier = Modifier.height(12.dp))

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(600, delayMillis = 200)) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(600, delayMillis = 200)
                )
            ) {
                GlassCard(
                    isDarkTheme = isThemeDark,
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 10.dp,
                    glowSize = 2.dp
                ) {
                    ThemeSwitcherMenuItem(
                        isDarkTheme = isThemeDark,
                        onToggle = { themeViewModel.toggleTheme() }
                    )
                }
            }
        }
    }
}
