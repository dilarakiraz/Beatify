package com.dilara.beatify.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.core.player.PlayerStateHolder
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.presentation.state.PlayerUIEvent
import com.dilara.beatify.presentation.state.PlayerUIState
import com.dilara.beatify.presentation.state.RepeatMode
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerStateHolder: PlayerStateHolder
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUIState())
    val uiState: StateFlow<PlayerUIState> = _uiState.asStateFlow()

    private val player by lazy { playerStateHolder.getPlayer() }

    init {
        observePlayerState()
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

    private fun playTrack(track: Track, playlist: List<Track>) {
        val url = track.previewUrl
        Log.d("PlayerViewModel", "Playing track: ${track.title}, URL: $url")
        
        if (url.isNullOrBlank()) {
            Log.e("PlayerViewModel", "Preview URL is empty for track: ${track.title}")
            _uiState.value = _uiState.value.copy(
                error = "Preview not available for this track",
                isLoading = false,
                isPlaying = false
            )
            return
        }

        val trackList = playlist.ifEmpty { listOf(track) }
        val index = trackList.indexOf(track)

        _uiState.value = _uiState.value.copy(
            currentTrack = track,
            playlist = trackList,
            currentIndex = index,
            isPlaying = false,
            isLoading = true,
            error = null,
            position = 0L
        )

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
                        Log.d("PlayerViewModel", "Player ready but playWhenReady false. Setting to true...")
                        p.playWhenReady = true
                    }

                    delay(200)
                    if (!p.isPlaying && p.playbackState == Player.STATE_READY) {
                        Log.d("PlayerViewModel", "Player ready but not playing. Force starting...")
                        val currentPos = p.currentPosition
                        p.seekTo(currentPos)
                        p.playWhenReady = true
                        _uiState.value = _uiState.value.copy(isPlaying = p.isPlaying)
                    } else if (p.isPlaying) {
                        _uiState.value = _uiState.value.copy(isPlaying = true)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("PlayerViewModel", "Error playing track: ${e.message}", e)
            _uiState.value = _uiState.value.copy(
                error = "Failed to play: ${e.message}",
                isLoading = false,
                isPlaying = false
            )
        }
    }

    private fun togglePlayPause() {
        val isPlaying = _uiState.value.isPlaying
        Log.d("PlayerViewModel", "Toggle play/pause. Current state: isPlaying=$isPlaying")
        
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
                Log.d("PlayerViewModel", "Playback state changed: $playbackState")
                when (playbackState) {
                    Player.STATE_READY -> {
                        Log.d("PlayerViewModel", "Player ready, duration: ${player.duration}, isPlaying: ${player.isPlaying}, playWhenReady: ${player.playWhenReady}, volume: ${player.volume}")
                        val duration = player.duration
                        if (duration > 0) {
                            // Update state immediately
                            _uiState.value = _uiState.value.copy(
                                duration = duration,
                                isLoading = false,
                                isBuffering = false,
                                error = null
                            )
                            
                            if (!player.playWhenReady) {
                                Log.d("PlayerViewModel", "Setting playWhenReady to true...")
                                player.playWhenReady = true
                            }
                            
                            if (player.playWhenReady && !player.isPlaying) {
                                Log.d("PlayerViewModel", "Player ready but not playing. Force starting playback...")
                                viewModelScope.launch {
                                    delay(100)
                                    
                                    if (player.playbackState == Player.STATE_READY && !player.isPlaying) {
                                        Log.d("PlayerViewModel", "Forcing playback by toggling playWhenReady...")
                                        player.playWhenReady = false
                                        delay(50)
                                        player.playWhenReady = true

                                        delay(150)
                                        if (!player.isPlaying && player.playbackState == Player.STATE_READY) {
                                            Log.d("PlayerViewModel", "Still not playing. Trying seek to trigger playback...")
                                            val pos = player.currentPosition
                                            player.seekTo(if (pos > 0) pos else 0)
                                            player.playWhenReady = true
                                        }
                                        
                                        _uiState.value = _uiState.value.copy(isPlaying = player.isPlaying)
                                    }
                                }
                            }
                            
                            _uiState.value = _uiState.value.copy(isPlaying = player.isPlaying && player.playWhenReady)
                        }
                    }
                    Player.STATE_BUFFERING -> {
                        Log.d("PlayerViewModel", "Player buffering")
                        _uiState.value = _uiState.value.copy(
                            isBuffering = true,
                            isLoading = true
                        )
                    }
                    Player.STATE_ENDED -> {
                        Log.d("PlayerViewModel", "Player ended")
                        _uiState.value = _uiState.value.copy(
                            isPlaying = false,
                            position = 0L
                        )
                        if (_uiState.value.repeatMode == RepeatMode.ALL) {
                            playNext()
                        }
                    }
                    Player.STATE_IDLE -> {
                        Log.d("PlayerViewModel", "Player idle")
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.d("PlayerViewModel", "Is playing changed: $isPlaying, playWhenReady: ${player.playWhenReady}, playbackState: ${player.playbackState}")
                _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
                
                if (!isPlaying && player.playWhenReady && player.playbackState == Player.STATE_READY) {
                    Log.d("PlayerViewModel", "Playback stopped unexpectedly. Attempting to restart...")
                    viewModelScope.launch {
                        delay(200)
                        if (!player.isPlaying && player.playWhenReady && player.playbackState == Player.STATE_READY) {
                            player.playWhenReady = false
                            delay(50)
                            player.playWhenReady = true
                            _uiState.value = _uiState.value.copy(isPlaying = player.isPlaying)
                        }
                    }
                }
            }
            
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                Log.d("PlayerViewModel", "PlayWhenReady changed: $playWhenReady, reason: $reason, playbackState: ${player.playbackState}, isPlaying: ${player.isPlaying}")
                _uiState.value = _uiState.value.copy(isPlaying = player.isPlaying)
                
                if (playWhenReady && player.playbackState == Player.STATE_READY && !player.isPlaying) {
                    Log.d("PlayerViewModel", "PlayWhenReady is true but not playing. Attempting to start...")
                    viewModelScope.launch {
                        delay(200)
                        if (player.playWhenReady && player.playbackState == Player.STATE_READY && !player.isPlaying) {
                            Log.d("PlayerViewModel", "Still not playing. Force starting...")
                            player.seekTo(player.currentPosition)
                            player.playWhenReady = true
                            _uiState.value = _uiState.value.copy(isPlaying = player.isPlaying)
                        }
                    }
                }
            }
            
            override fun onPlayerError(error: com.google.android.exoplayer2.PlaybackException) {
                Log.e("PlayerViewModel", "ExoPlayer error: ${error.message}", error)
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
        playerStateHolder.releasePlayer()
    }
}

