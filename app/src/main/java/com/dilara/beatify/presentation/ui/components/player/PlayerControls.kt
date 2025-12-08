package com.dilara.beatify.presentation.ui.components.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dilara.beatify.R
import com.dilara.beatify.presentation.state.RepeatMode
import com.dilara.beatify.ui.theme.NeonCyan
import com.dilara.beatify.ui.theme.NeonPink
import com.dilara.beatify.ui.theme.NeonPurple
import com.dilara.beatify.ui.theme.NeonTextSecondary
import com.dilara.beatify.core.utils.stringResource

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    repeatMode: RepeatMode,
    isShuffleEnabled: Boolean,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onRepeatClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onShuffleClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_shuffle),
                contentDescription = stringResource(R.string.cd_shuffle),
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(
                    if (isShuffleEnabled) NeonCyan else NeonTextSecondary
                )
            )
        }

        IconButton(onClick = onPreviousClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_skip_previous),
                contentDescription = stringResource(R.string.cd_previous),
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(NeonPink)
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
                    contentDescription = stringResource(R.string.cd_play),
                    tint = NeonPink,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        IconButton(onClick = onNextClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_skip_next),
                contentDescription = stringResource(R.string.cd_next),
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(NeonPink)
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
                contentDescription = stringResource(R.string.cd_repeat),
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(
                    if (repeatMode != RepeatMode.OFF) NeonCyan else NeonTextSecondary
                )
            )
        }
    }
}

