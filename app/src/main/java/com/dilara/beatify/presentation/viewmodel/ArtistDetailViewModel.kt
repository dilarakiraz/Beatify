package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.presentation.state.ArtistDetailUIEvent
import com.dilara.beatify.presentation.state.ArtistDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistDetailUIState())
    val uiState: StateFlow<ArtistDetailUIState> = _uiState.asStateFlow()

    fun onEvent(event: ArtistDetailUIEvent) {
        when (event) {
            is ArtistDetailUIEvent.LoadArtist -> {
                loadArtistDetails(event.artistId)
            }
            is ArtistDetailUIEvent.Refresh -> {
                loadArtistDetails(event.artistId, isRefresh = true)
            }
        }
    }

    private fun loadArtistDetails(artistId: Long, isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = !isRefresh, error = null) }

            // Load artist info
            musicRepository.getArtistById(artistId).fold(
                onSuccess = { artist ->
                    _uiState.update { it.copy(artist = artist) }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            error = exception.message ?: "Sanatçı bilgileri yüklenemedi",
                            isLoading = false
                        )
                    }
                    return@launch
                }
            )

            musicRepository.getArtistTopTracks(artistId, limit = 25).fold(
                onSuccess = { tracks ->
                    _uiState.update { it.copy(topTracks = tracks) }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(topTracks = emptyList()) }
                }
            )

            musicRepository.getArtistAlbums(artistId, limit = 50).fold(
                onSuccess = { albums ->
                    _uiState.update { it.copy(albums = albums) }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(albums = emptyList()) }
                }
            )

            musicRepository.getRelatedArtists(artistId, limit = 10).fold(
                onSuccess = { artists ->
                    _uiState.update { it.copy(relatedArtists = artists) }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(relatedArtists = emptyList()) }
                }
            )

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}

