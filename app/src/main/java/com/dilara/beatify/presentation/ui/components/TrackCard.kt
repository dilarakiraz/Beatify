package com.dilara.beatify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.NeonTextSecondary

@Composable
fun TrackCard(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = 0.05f),
                            Transparent,
                            NeonCyan.copy(alpha = 0.05f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = track.album.coverMedium ?: track.album.cover,
                    contentDescription = track.album.title,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = track.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonTextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = track.artist.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = NeonTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Duration
                Text(
                    text = formatDuration(track.duration),
                    fontSize = 12.sp,
                    color = NeonTextSecondary,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun TrackCardHorizontal(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = 0.1f),
                            Transparent,
                            NeonCyan.copy(alpha = 0.1f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = track.album.coverBig ?: track.album.coverMedium ?: track.album.cover,
                    contentDescription = track.album.title,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = track.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonTextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Text(
                        text = track.artist.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = NeonTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = track.album.title,
                        fontSize = 12.sp,
                        color = NeonTextSecondary.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = formatDuration(track.duration),
                    fontSize = 13.sp,
                    color = NeonTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", minutes, secs)
}

