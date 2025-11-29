package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.domain.repository.PlaylistRepository
import com.dilara.beatify.presentation.state.PlaylistDetailUIEvent
import com.dilara.beatify.presentation.state.PlaylistDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaylistDetailUIState())
    val uiState: StateFlow<PlaylistDetailUIState> = _uiState.asStateFlow()

    fun onEvent(event: PlaylistDetailUIEvent) {
        when (event) {
            is PlaylistDetailUIEvent.LoadPlaylist -> loadPlaylist(event.playlistId)
            is PlaylistDetailUIEvent.AddTrack -> addTrack(event.track)
            is PlaylistDetailUIEvent.RemoveTrack -> removeTrack(event.trackId)
            is PlaylistDetailUIEvent.OnTrackClick -> { /* Handled by navigation */ }
            is PlaylistDetailUIEvent.DeletePlaylist -> deletePlaylist(event.playlistId)
            is PlaylistDetailUIEvent.UpdatePlaylistName -> updatePlaylistName(event.playlistId, event.name)
            is PlaylistDetailUIEvent.ReorderTracks -> reorderTracks(event.fromIndex, event.toIndex)
        }
    }

    private fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val playlist = playlistRepository.getPlaylistById(playlistId)
            val tracks = playlistRepository.getPlaylistTracks(playlistId)

            if (playlist != null) {
                _uiState.value = _uiState.value.copy(
                    playlist = playlist.copy(trackCount = tracks.size),
                    tracks = tracks,
                    isLoading = false,
                    error = null
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Çalma listesi bulunamadı"
                )
            }
        }
    }

    private fun addTrack(track: Track) {
        val playlist = _uiState.value.playlist ?: return
        viewModelScope.launch {
            playlistRepository.addTrackToPlaylist(playlist.id, track).fold(
                onSuccess = {
                    loadPlaylist(playlist.id)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Şarkı eklenemedi"
                    )
                }
            )
        }
    }

    private fun removeTrack(trackId: Long) {
        val playlist = _uiState.value.playlist ?: return
        viewModelScope.launch {
            playlistRepository.removeTrackFromPlaylist(playlist.id, trackId).fold(
                onSuccess = {
                    loadPlaylist(playlist.id)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Şarkı kaldırılamadı"
                    )
                }
            )
        }
    }

    private fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlistId).fold(
                onSuccess = { /* Handled by navigation */ },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Çalma listesi silinemedi"
                    )
                }
            )
        }
    }

    private fun updatePlaylistName(playlistId: Long, name: String) {
        viewModelScope.launch {
            playlistRepository.updatePlaylistName(playlistId, name).fold(
                onSuccess = {
                    loadPlaylist(playlistId)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Çalma listesi adı güncellenemedi"
                    )
                }
            )
        }
    }
    
    private fun reorderTracks(fromIndex: Int, toIndex: Int) {
        val playlist = _uiState.value.playlist ?: return
        
        val currentTracks = _uiState.value.tracks.toMutableList()
        if (fromIndex in currentTracks.indices && toIndex in currentTracks.indices) {
            val movedTrack = currentTracks.removeAt(fromIndex)
            currentTracks.add(toIndex, movedTrack)
            _uiState.value = _uiState.value.copy(tracks = currentTracks)
        }
        
        viewModelScope.launch {
            playlistRepository.reorderPlaylistTracks(playlist.id, fromIndex, toIndex).fold(
                onSuccess = { /* Already updated optimistically */ },
                onFailure = { exception ->
                    loadPlaylist(playlist.id)
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Sıralama değiştirilemedi"
                    )
                }
            )
        }
    }
}

