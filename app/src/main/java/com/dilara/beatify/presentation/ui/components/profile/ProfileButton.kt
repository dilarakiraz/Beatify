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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonTextSecondary

@Composable
fun ProfileButton(
    profileImageUri: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(DarkSurface.copy(alpha = 0.6f))
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
                tint = NeonTextSecondary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

