package com.dilara.beatify.presentation.ui.navigation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val selectedIcon: ImageVector = icon
)


