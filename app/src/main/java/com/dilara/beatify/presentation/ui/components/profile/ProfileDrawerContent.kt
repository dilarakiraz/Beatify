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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dilara.beatify.presentation.ui.components.common.GlassCard
import com.dilara.beatify.presentation.ui.components.profile.AppLanguage
import com.dilara.beatify.presentation.viewmodel.LanguageViewModel
import com.dilara.beatify.presentation.viewmodel.ThemeViewModel
import com.dilara.beatify.ui.theme.DarkBackground
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.DarkSurfaceVariant
import com.dilara.beatify.ui.theme.LightBackground
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.LightSurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDrawerContent(
    profileImageUri: String?,
    onDismiss: () -> Unit,
    themeViewModel: ThemeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val isThemeDark by themeViewModel.isDarkTheme.collectAsState()
    val languageViewModel: LanguageViewModel = hiltViewModel()
    val selectedLanguage by languageViewModel.currentLanguage.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (isThemeDark) {
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground,
                            DarkSurface,
                            DarkSurfaceVariant
                        )
                    )
                } else {
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            LightBackground,
                            LightSurface,
                            LightSurfaceVariant.copy(alpha = 0.5f)
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
                    size = 72.dp,
                    glowSize = 88.dp,
                    iconSize = 40.dp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(500, delayMillis = 100)) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(500, delayMillis = 100)
                )
            ) {
                GlassCard(
                    isDarkTheme = isThemeDark,
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 10.dp,
                    glowSize = 2.dp
                ) {
                    LanguageSelector(
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { languageViewModel.setLanguage(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
