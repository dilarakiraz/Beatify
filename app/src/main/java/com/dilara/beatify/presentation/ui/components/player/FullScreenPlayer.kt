package com.dilara.beatify.presentation.ui.components.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dilara.beatify.R
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.state.RepeatMode
import com.dilara.beatify.presentation.ui.components.common.FavoriteButton
import com.dilara.beatify.ui.theme.DarkBackground
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPink
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonTextPrimary
import com.dilara.beatify.ui.theme.NeonTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenPlayer(
    track: Track?,
    isPlaying: Boolean,
    position: Long,
    duration: Long,
    repeatMode: RepeatMode,
    isShuffleEnabled: Boolean,
    error: String? = null,
    isFavorite: Boolean = false,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onRepeatClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (track == null) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = Transparent,
        dragHandle = {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .padding(vertical = 12.dp)
                    .background(
                        NeonCyan.copy(alpha = 0.6f),
                        RoundedCornerShape(2.dp)
                    )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground.copy(alpha = 0.98f),
                            DarkSurface.copy(alpha = 0.96f),
                            DarkBackground.copy(alpha = 0.98f)
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                NeonPurple.copy(alpha = 0.08f),
                                NeonCyan.copy(alpha = 0.05f),
                                Transparent
                            ),
                            radius = 800f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            AlbumCoverWithSpin(
                coverUrl = track.album.coverBig ?: track.album.cover,
                isPlaying = isPlaying,
                modifier = Modifier
                    .size(180.dp)
                    .padding(vertical = 8.dp)
            )

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = track.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = track.artist.name,
                            fontSize = 16.sp,
                            color = NeonTextSecondary
                        )
                    }
                    
                    FavoriteButton(
                        isFavorite = isFavorite,
                        onClick = onFavoriteClick,
                        size = 48.dp,
                        iconSize = 28.dp
                    )
                }

                if (error != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error,
                        fontSize = 14.sp,
                        color = NeonPink,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                SeekBar(
                    position = position,
                    duration = duration,
                    onSeekTo = onSeekTo,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onShuffleClick) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_shuffle),
                            contentDescription = "Shuffle",
                            modifier = Modifier.size(28.dp),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                                if (isShuffleEnabled) NeonCyan else NeonTextSecondary
                            )
                        )
                    }

                    IconButton(onClick = onPreviousClick) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_skip_previous),
                            contentDescription = "Previous",
                            modifier = Modifier.size(32.dp),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(NeonPink)
                        )
                    }

                    IconButton(
                        onClick = onPlayPauseClick,
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        NeonCyan.copy(alpha = 0.4f),
                                        NeonPurple.copy(alpha = 0.2f)
                                    )
                                ),
                                shape = CircleShape
                            )
                    ) {
                        if (isPlaying) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(8.dp)
                                        .height(28.dp)
                                        .background(NeonCyan, RoundedCornerShape(2.dp))
                                )
                                Box(
                                    modifier = Modifier
                                        .width(8.dp)
                                        .height(28.dp)
                                        .background(NeonCyan, RoundedCornerShape(2.dp))
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = NeonPink,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    IconButton(onClick = onNextClick) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_skip_next),
                            contentDescription = "Next",
                            modifier = Modifier.size(32.dp),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(NeonPink)
                        )
                    }

                    IconButton(onClick = onRepeatClick) {
                        Image(
                            painter = painterResource(
                                id = when (repeatMode) {
                                    RepeatMode.OFF -> R.drawable.ic_repeat
                                    RepeatMode.ALL -> R.drawable.ic_repeat
                                    RepeatMode.ONE -> R.drawable.ic_repeat_one
                                }
                            ),
                            contentDescription = "Repeat",
                            modifier = Modifier.size(28.dp),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                                if (repeatMode != RepeatMode.OFF) NeonCyan else NeonTextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AlbumCoverWithSpin(
    coverUrl: String?,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = coverUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .rotate(if (isPlaying) rotationAngle else 0f),
            contentScale = ContentScale.Crop
        )

        if (isPlaying) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.2f),
                                NeonPurple.copy(alpha = 0.1f),
                                Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun SeekBar(
    position: Long,
    duration: Long,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (duration > 0) (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f) else 0f

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(position),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = NeonCyan
            )
            Text(
                text = formatTime(duration),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = NeonTextSecondary
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val trackWidth = maxWidth

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { tapOffset ->
                            if (duration > 0 && constraints.maxWidth > 0) {
                                val newProgress = (tapOffset.x / constraints.maxWidth).coerceIn(0f, 1f)
                                val newPosition = (newProgress * duration).toLong()
                                onSeekTo(newPosition)
                            }
                        }
                    }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(DarkSurface.copy(alpha = 0.4f))
            )

            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width((trackWidth * progress).coerceAtMost(trackWidth))
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                NeonCyan,
                                NeonPurple,
                                Color(0xFF6A9BD5)  // Soft blue - eye-friendly
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .offset(x = (trackWidth * progress - 10.dp).coerceAtLeast(0.dp).coerceAtMost(trackWidth - 20.dp))
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.9f),
                                NeonCyan.copy(alpha = 0.7f)
                            )
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.4f),
                                    Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}

