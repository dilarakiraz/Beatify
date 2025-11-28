package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Track

data class PlayerUIState(
    val currentTrack: Track? = null,
    val playlist: List<Track> = emptyList(),
    val currentIndex: Int = -1,
    val isPlaying: Boolean = false,
    val isExpanded: Boolean = false,
    val position: Long = 0L, // in milliseconds
    val duration: Long = 0L, // in milliseconds
    val isLoading: Boolean = false,
    val isBuffering: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val isShuffleEnabled: Boolean = false,
    val error: String? = null
)

enum class RepeatMode {
    OFF,
    ONE,
    ALL
}

sealed class PlayerUIEvent {
    data class PlayTrack(val track: Track, val playlist: List<Track> = emptyList()) : PlayerUIEvent()
    data object PlayPause : PlayerUIEvent()
    data object Next : PlayerUIEvent()
    data object Previous : PlayerUIEvent()
    data class SeekTo(val position: Long) : PlayerUIEvent()
    data object Expand : PlayerUIEvent()
    data object Collapse : PlayerUIEvent()
    data object ToggleRepeat : PlayerUIEvent()
    data object ToggleShuffle : PlayerUIEvent()
}

