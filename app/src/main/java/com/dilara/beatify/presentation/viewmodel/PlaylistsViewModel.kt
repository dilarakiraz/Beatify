package com.dilara.beatify.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.R
import com.dilara.beatify.domain.repository.PlaylistRepository
import com.dilara.beatify.presentation.state.PlaylistsUIEvent
import com.dilara.beatify.presentation.state.PlaylistsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel @Inject constructor(
    application: Application,
    private val playlistRepository: PlaylistRepository
) : AndroidViewModel(application) {
    
    private val _uiState = MutableStateFlow(PlaylistsUIState())
    val uiState: StateFlow<PlaylistsUIState> = _uiState.asStateFlow()
    
    init {
        loadPlaylists()
    }
    
    fun onEvent(event: PlaylistsUIEvent) {
        when (event) {
            is PlaylistsUIEvent.LoadPlaylists -> loadPlaylists()
            is PlaylistsUIEvent.CreatePlaylist -> createPlaylist(event.name)
            is PlaylistsUIEvent.DeletePlaylist -> deletePlaylist(event.playlistId)
            is PlaylistsUIEvent.OnPlaylistClick -> { /* Handled by navigation */ }
            is PlaylistsUIEvent.ReorderPlaylists -> reorderPlaylists(event.fromIndex, event.toIndex)
        }
    }
    
    private fun loadPlaylists() {
        playlistRepository.getAllPlaylists()
            .onStart { _uiState.value = _uiState.value.copy(isLoading = true, error = null) }
            .catch { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: getApplication<Application>().getString(R.string.error_playlist_load_failed)
                )
            }
            .onEach { playlists ->
                _uiState.value = _uiState.value.copy(
                    playlists = playlists,
                    isLoading = false,
                    error = null
                )
            }
            .launchIn(viewModelScope)
    }
    
    private fun createPlaylist(name: String) {
        if (name.isBlank()) return
        
        viewModelScope.launch {
            playlistRepository.createPlaylist(name, null).fold(
                onSuccess = { /* Playlist will be reloaded automatically */ },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: getApplication<Application>().getString(R.string.error_playlist_create_failed)
                    )
                }
            )
        }
    }
    
    private fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlistId).fold(
                onSuccess = { /* Playlist will be reloaded automatically */ },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: getApplication<Application>().getString(R.string.error_playlist_delete_failed)
                    )
                }
            )
        }
    }
    
    private fun reorderPlaylists(fromIndex: Int, toIndex: Int) {
        val currentPlaylists = _uiState.value.playlists.toMutableList()
        if (fromIndex in currentPlaylists.indices && toIndex in currentPlaylists.indices) {
            val movedPlaylist = currentPlaylists.removeAt(fromIndex)
            currentPlaylists.add(toIndex, movedPlaylist)
            _uiState.value = _uiState.value.copy(playlists = currentPlaylists)
        }
        
        viewModelScope.launch {
            playlistRepository.reorderPlaylists(fromIndex, toIndex).fold(
                onSuccess = { /* Already updated optimistically */ },
                onFailure = { exception ->
                    loadPlaylists()
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: getApplication<Application>().getString(R.string.error_reorder_failed)
                    )
                }
            )
        }
    }
}
