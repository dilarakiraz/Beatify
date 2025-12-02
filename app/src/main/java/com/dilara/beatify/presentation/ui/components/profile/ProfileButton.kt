package com.dilara.beatify.presentation.ui.components.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileButton(
    profileImageUri: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileAvatar(
        profileImageUri = profileImageUri,
        onClick = onClick,
        modifier = modifier,
        size = 48.dp,
        glowSize = 60.dp,
        iconSize = 28.dp
    )
}

