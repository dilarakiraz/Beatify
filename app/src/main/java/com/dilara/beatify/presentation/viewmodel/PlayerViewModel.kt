package com.dilara.beatify.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.R
import com.dilara.beatify.core.player.PlayerStateHolder
import com.dilara.beatify.core.service.NotificationAction
import com.dilara.beatify.core.service.NotificationActionBus
import com.dilara.beatify.core.service.ServiceHelper
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.domain.repository.RecentTracksRepository
import com.dilara.beatify.presentation.state.PlayerUIEvent
import com.dilara.beatify.presentation.state.PlayerUIState
import com.dilara.beatify.presentation.state.RepeatMode
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    application: Application,
    private val playerStateHolder: PlayerStateHolder,
    private val musicRepository: MusicRepository,
    private val recentTracksRepository: RecentTracksRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(PlayerUIState())
    val uiState: StateFlow<PlayerUIState> = _uiState.asStateFlow()

    private val player by lazy { playerStateHolder.getPlayer() }

    init {
        observePlayerState()
        observeNotificationActions()
    }

    fun onEvent(event: PlayerUIEvent) {
        when (event) {
            is PlayerUIEvent.PlayTrack -> {
                playTrack(event.track, event.playlist)
            }
            
            is PlayerUIEvent.PlayPause -> {
                togglePlayPause()
            }
            
            is PlayerUIEvent.Next -> {
                playNext()
            }
            
            is PlayerUIEvent.Previous -> {
                playPrevious()
            }
            
            is PlayerUIEvent.SeekTo -> {
                seekTo(event.position)
            }
            
            is PlayerUIEvent.Expand -> {
                _uiState.value = _uiState.value.copy(isExpanded = true)
            }
            
            is PlayerUIEvent.Collapse -> {
                _uiState.value = _uiState.value.copy(isExpanded = false)
            }
            
            is PlayerUIEvent.ToggleRepeat -> {
                toggleRepeat()
            }
            
            is PlayerUIEvent.ToggleShuffle -> {
                toggleShuffle()
            }
        }
    }

    private fun observeNotificationActions() {
        viewModelScope.launch {
            NotificationActionBus.actions.collectLatest { action ->
                when (action) {
                    NotificationAction.PLAY_PAUSE -> {
                        // Service already toggled, just sync UI state with player state
                        val player = playerStateHolder.getPlayer()
                        _uiState.value = _uiState.value.copy(isPlaying = player.isPlaying)
                        ServiceHelper.updateNotification(
                            _uiState.value.currentTrack,
                            player.isPlaying,
                            _uiState.value.playlist,
                            _uiState.value.currentIndex
                        )
                    }
                    NotificationAction.NEXT -> onEvent(PlayerUIEvent.Next)
                    NotificationAction.PREVIOUS -> onEvent(PlayerUIEvent.Previous)
                    NotificationAction.STOP -> onEvent(PlayerUIEvent.Collapse)
                }
            }
        }
    }

    private fun playTrack(track: Track, playlist: List<Track>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                currentTrack = track,
                isLoading = true,
                error = null,
                isPlaying = false
            )

            val trackToPlay = fetchFreshTrack(track)
            val url = trackToPlay.previewUrl
            
            if (url.isNullOrBlank()) {
                _uiState.value = _uiState.value.copy(
                    error = getApplication<Application>().getString(R.string.error_track_preview_not_found),
                    isLoading = false,
                    isPlaying = false
                )
                return@launch
            }
            
            if (trackToPlay != track) {
                _uiState.value = _uiState.value.copy(currentTrack = trackToPlay)
            }
    
            val trackList = playlist.ifEmpty { listOf(trackToPlay) }
            val index = trackList.indexOfFirst { it.id == trackToPlay.id }.takeIf { it != -1 } ?: 0
    
            _uiState.value = _uiState.value.copy(
                playlist = trackList,
                currentIndex = index,
                position = 0L,
                currentTrack = trackToPlay  // Ensure currentTrack is updated
            )
            
            recentTracksRepository.addRecentTrack(trackToPlay)
            
            // Update notification immediately with new track
            ServiceHelper.updateNotification(trackToPlay, _uiState.value.isPlaying, trackList, index)
            
            // Start foreground service (will update notification if service already running)
            ServiceHelper.startService(getApplication(), trackToPlay, trackList, index)
            
            startPlayback(url)
        }
    }

    private suspend fun fetchFreshTrack(track: Track): Track {
        return try {
            val result = musicRepository.getTrackById(track.id)
            if (result.isSuccess) {
                result.getOrNull() ?: track
            } else {
                track
            }
        } catch (e: Exception) {
            e.printStackTrace()
            track
        }
    }

    private fun startPlayback(url: String) {
        try {
            playerStateHolder.play(url)
            
            viewModelScope.launch {
                val p = playerStateHolder.getPlayer()
                
                var attempts = 0
                while (attempts < 30 && p.playbackState != Player.STATE_READY) {
                    delay(100)
                    attempts++
                    
                    if (!p.playWhenReady) {
                        p.playWhenReady = true
                    }
                }
                
                if (p.playbackState == Player.STATE_READY) {
                    if (!p.playWhenReady) {
                        p.playWhenReady = true
                    }

                    delay(200)
                    if (!p.isPlaying && p.playbackState == Player.STATE_READY) {
                        val currentPos = p.currentPosition
                        p.seekTo(currentPos)
                        p.playWhenReady = true
                        _uiState.value = _uiState.value.copy(isPlaying = p.isPlaying)
                    } else if (p.isPlaying) {
                        _uiState.value = _uiState.value.copy(isPlaying = true)
                    }
                    
                    // Update notification when playback is ready
                    ServiceHelper.updateNotification(
                        _uiState.value.currentTrack,
                        p.isPlaying,
                        _uiState.value.playlist,
                        _uiState.value.currentIndex
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = getApplication<Application>().getString(R.string.error_playback_failed, e.message ?: ""),
                isLoading = false,
                isPlaying = false
            )
        }
    }

    private fun togglePlayPause() {
        val isPlaying = _uiState.value.isPlaying
        
        if (isPlaying) {
            playerStateHolder.pause()
            _uiState.value = _uiState.value.copy(isPlaying = false)
        } else {
            playerStateHolder.resume()
            _uiState.value = _uiState.value.copy(isPlaying = true)
        }
    }

    private fun playNext() {
        val state = _uiState.value
        if (state.playlist.isEmpty() || state.currentIndex == -1) return

        val nextIndex = if (state.isShuffleEnabled) {
            state.playlist.indices.random()
        } else {
            when (state.repeatMode) {
                RepeatMode.ONE -> state.currentIndex
                RepeatMode.ALL -> (state.currentIndex + 1) % state.playlist.size
                RepeatMode.OFF -> {
                    if (state.currentIndex < state.playlist.size - 1) {
                        state.currentIndex + 1
                    } else {
                        return
                    }
                }
            }
        }

        if (nextIndex in state.playlist.indices) {
            playTrack(state.playlist[nextIndex], state.playlist)
        }
    }

    private fun playPrevious() {
        val state = _uiState.value
        if (state.playlist.isEmpty() || state.currentIndex == -1) return

        val prevIndex = if (state.currentIndex > 0) {
            state.currentIndex - 1
        } else if (state.repeatMode == RepeatMode.ALL) {
            state.playlist.size - 1
        } else {
            return
        }

        if (prevIndex in state.playlist.indices) {
            playTrack(state.playlist[prevIndex], state.playlist)
        }
    }

    private fun seekTo(position: Long) {
        playerStateHolder.seekTo(position)
        _uiState.value = _uiState.value.copy(position = position)
    }

    private fun toggleRepeat() {
        val currentMode = _uiState.value.repeatMode
        val nextMode = when (currentMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        _uiState.value = _uiState.value.copy(repeatMode = nextMode)
    }

    private fun toggleShuffle() {
        _uiState.value = _uiState.value.copy(
            isShuffleEnabled = !_uiState.value.isShuffleEnabled
        )
    }

    private fun observePlayerState() {
        val playerListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        val duration = player.duration
                        if (duration > 0) {
                            _uiState.value = _uiState.value.copy(
                                duration = duration,
                                isLoading = false,
                                isBuffering = false,
                                error = null
                            )
                            
                            if (!player.playWhenReady) {
                                player.playWhenReady = true
                            }
                            
                            if (player.playWhenReady && !player.isPlaying) {
                                viewModelScope.launch {
                                    delay(100)
                                    
                                    if (player.playbackState == Player.STATE_READY && !player.isPlaying) {
                                        player.playWhenReady = false
                                        delay(50)
                                        player.playWhenReady = true

                                        delay(150)
                                        if (!player.isPlaying && player.playbackState == Player.STATE_READY) {
                                            val pos = player.currentPosition
                                            player.seekTo(if (pos > 0) pos else 0)
                                            player.playWhenReady = true
                                        }
                                        
                                        _uiState.value = _uiState.value.copy(isPlaying = player.isPlaying)
                                    }
                                }
                            }
                            
                            _uiState.value = _uiState.value.copy(isPlaying = player.isPlaying && player.playWhenReady)
                            
                            // Update notification when ready
                            ServiceHelper.updateNotification(
                                _uiState.value.currentTrack,
                                player.isPlaying,
                                _uiState.value.playlist,
                                _uiState.value.currentIndex
                            )
                        }
                    }
                    Player.STATE_BUFFERING -> {
                        _uiState.value = _uiState.value.copy(
                            isBuffering = true,
                            isLoading = true
                        )
                    }
                    Player.STATE_ENDED -> {
                        _uiState.value = _uiState.value.copy(
                            isPlaying = false,
                            position = 0L
                        )
                        if (_uiState.value.repeatMode == RepeatMode.ALL) {
                            playNext()
                        }
                    }
                    Player.STATE_IDLE -> {
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
                
                // Update notification
                ServiceHelper.updateNotification(
                    _uiState.value.currentTrack,
                    isPlaying,
                    _uiState.value.playlist,
                    _uiState.value.currentIndex
                )
                
                // Removed the playWhenReady toggle logic as it was causing conflicts
                // Service handles play/pause directly, ViewModel just syncs UI state
            }
            
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                _uiState.value = _uiState.value.copy(isPlaying = player.isPlaying)
                
                if (playWhenReady && player.playbackState == Player.STATE_READY && !player.isPlaying) {
                    viewModelScope.launch {
                        delay(200)
                        if (player.playWhenReady && player.playbackState == Player.STATE_READY && !player.isPlaying) {
                            player.seekTo(player.currentPosition)
                            player.playWhenReady = true
                            _uiState.value = _uiState.value.copy(isPlaying = player.isPlaying)
                        }
                    }
                }
            }
            
            override fun onPlayerError(error: com.google.android.exoplayer2.PlaybackException) {
                _uiState.value = _uiState.value.copy(
                    error = error.message ?: "Playback error: ${error.errorCode}",
                    isLoading = false,
                    isPlaying = false
                )
            }
        }

        player.addListener(playerListener)

        viewModelScope.launch {
            while (true) {
                if (player.isPlaying) {
                    val position = player.currentPosition
                    _uiState.value = _uiState.value.copy(position = position)
                }
                delay(100)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ServiceHelper.stopService(getApplication())
        playerStateHolder.releasePlayer()
    }
}

