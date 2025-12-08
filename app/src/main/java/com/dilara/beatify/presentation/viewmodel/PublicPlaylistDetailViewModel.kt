package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.presentation.state.PublicPlaylistDetailUIEvent
import com.dilara.beatify.presentation.state.PublicPlaylistDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicPlaylistDetailViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PublicPlaylistDetailUIState())
    val uiState: StateFlow<PublicPlaylistDetailUIState> = _uiState.asStateFlow()
    
    private var currentPlaylistId: Long = -1

    fun onEvent(event: PublicPlaylistDetailUIEvent) {
        when (event) {
            is PublicPlaylistDetailUIEvent.LoadPlaylist -> {
                // Eğer farklı bir playlist'e geçiliyorsa state'i resetle
                if (currentPlaylistId != event.playlistId) {
                    _uiState.value = PublicPlaylistDetailUIState()
                }
                currentPlaylistId = event.playlistId
                
                if (event.initialPlaylistTitle != null) {
                    _uiState.value = PublicPlaylistDetailUIState(
                        isLoading = true,
                        playlist = com.dilara.beatify.domain.model.PublicPlaylist(
                            id = event.playlistId,
                            title = event.initialPlaylistTitle,
                            cover = null,
                            coverSmall = null,
                            coverMedium = null,
                            coverBig = null,
                            coverXl = null,
                            trackCount = 0,
                            creatorName = null
                        )
                    )
                }
                loadPlaylistDetails(event.playlistId, event.initialPlaylistTitle)
            }
            is PublicPlaylistDetailUIEvent.OnTrackClick -> {
                // Handled by navigation
            }
            is PublicPlaylistDetailUIEvent.Retry -> {
                if (currentPlaylistId != -1L) {
                    loadPlaylistDetails(currentPlaylistId, null)
                }
            }
        }
    }

    private fun loadPlaylistDetails(playlistId: Long, initialPlaylistTitle: String?) {
        viewModelScope.launch {
            // Playlist bilgisi zaten varsa (initialPlaylistTitle'dan), sadece tracks için loading yap
            _uiState.value = _uiState.value.copy(
                isLoading = _uiState.value.playlist == null,
                error = null
            )

            // Playlist bilgisini ve tracks'i paralel yükle
            val playlistDeferred = async {
                musicRepository.getPublicPlaylist(playlistId)
            }
            
            val tracksResult = musicRepository.getPublicPlaylistTracks(playlistId)
            
            // Playlist bilgisini güncelle
            playlistDeferred.await().onSuccess { playlist ->
                _uiState.value = _uiState.value.copy(playlist = playlist)
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    error = exception.message ?: "Playlist yüklenirken bir hata oluştu"
                )
            }

            tracksResult.onSuccess { tracks ->
                _uiState.value = _uiState.value.copy(
                    tracks = tracks,
                    isLoading = false
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Playlist şarkıları yüklenirken bir hata oluştu"
                )
            }
        }
    }
}

