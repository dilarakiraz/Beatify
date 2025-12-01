package com.dilara.beatify.presentation.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onArtistClick: (() -> Unit)? = null,
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
            VinylRecordCover(
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
                            color = NeonTextSecondary,
                            modifier = Modifier
                                .then(
                                    if (onArtistClick != null) {
                                        Modifier.clickable(onClick = onArtistClick)
                                    } else {
                                        Modifier
                                    }
                                )
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

                PlayerSeekBar(
                    position = position,
                    duration = duration,
                    onSeekTo = onSeekTo,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                PlayerControls(
                    isPlaying = isPlaying,
                    repeatMode = repeatMode,
                    isShuffleEnabled = isShuffleEnabled,
                    onPlayPauseClick = onPlayPauseClick,
                    onNextClick = onNextClick,
                    onPreviousClick = onPreviousClick,
                    onRepeatClick = onRepeatClick,
                    onShuffleClick = onShuffleClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}


