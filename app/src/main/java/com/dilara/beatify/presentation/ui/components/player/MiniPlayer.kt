package com.dilara.beatify.presentation.ui.components.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dilara.beatify.R
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.state.RepeatMode
import com.dilara.beatify.ui.theme.BeatifyGradients
import com.dilara.beatify.core.utils.stringResource
import com.dilara.beatify.ui.theme.DarkSurface
import com.dilara.beatify.ui.theme.LightSurface
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPink
import com.dilara.beatify.ui.theme.NeonTextSecondary
import com.dilara.beatify.ui.theme.isDarkTheme
import com.dilara.beatify.ui.theme.themeTextPrimary
import com.dilara.beatify.ui.theme.themeTextSecondary

@Composable
fun MiniPlayer(
    track: Track?,
    isPlaying: Boolean,
    repeatMode: RepeatMode = RepeatMode.OFF,
    isShuffleEnabled: Boolean = false,
    onPlayPauseClick: () -> Unit,
    onRepeatClick: () -> Unit = {},
    onShuffleClick: () -> Unit = {},
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (track == null) return

    AnimatedVisibility(
        visible = track != null,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = if (isDarkTheme) {
                        Brush.horizontalGradient(
                            colors = listOf(
                                DarkSurface.copy(alpha = 0.95f),
                                DarkSurface.copy(alpha = 0.9f)
                            )
                        )
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(
                                LightSurface.copy(alpha = 1f),
                                LightSurface.copy(alpha = 0.98f)
                            )
                        )
                    }
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(brush = BeatifyGradients.miniPlayerGradient(isDarkTheme))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onExpandClick() }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = { onExpandClick() }
                        ) { change, dragAmount ->
                            if (dragAmount.y < 0) {
                                onExpandClick()
                            }
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
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
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = themeTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = track.artist.name,
                            fontSize = 12.sp,
                            color = themeTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(
                        onClick = { 
                            onShuffleClick()
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_shuffle),
                            contentDescription = stringResource(R.string.cd_shuffle),
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(
                                if (isShuffleEnabled) NeonCyan else NeonTextSecondary.copy(alpha = 0.6f)
                            )
                        )
                    }

                    IconButton(
                        onClick = { onPlayPauseClick() },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        NeonCyan.copy(alpha = 0.3f),
                                        Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    ) {
                        if (isPlaying) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(6.dp)
                                        .height(20.dp)
                                        .background(NeonCyan, RoundedCornerShape(2.dp))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(6.dp)
                                        .height(20.dp)
                                        .background(NeonCyan, RoundedCornerShape(2.dp))
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = stringResource(R.string.cd_play),
                                tint = NeonPink,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { 
                            onRepeatClick()
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                id = when (repeatMode) {
                                    RepeatMode.OFF -> R.drawable.ic_repeat
                                    RepeatMode.ALL -> R.drawable.ic_repeat
                                    RepeatMode.ONE -> R.drawable.ic_repeat_one
                                }
                            ),
                            contentDescription = stringResource(R.string.cd_repeat),
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(
                                if (repeatMode != RepeatMode.OFF) NeonCyan else NeonTextSecondary.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    }
}

