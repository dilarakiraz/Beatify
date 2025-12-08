package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.presentation.state.HomeUIEvent
import com.dilara.beatify.presentation.state.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun onEvent(event: HomeUIEvent) {
        when (event) {
            is HomeUIEvent.OnTrackClick -> {
                // TODO: Navigate to track detail or play track
            }

            is HomeUIEvent.OnArtistClick -> {
                // TODO: Navigate to artist detail
            }

            is HomeUIEvent.OnAlbumClick -> {
                // TODO: Navigate to album detail
            }
            
            is HomeUIEvent.OnGenreClick -> {
                // Handled by navigation
            }

            is HomeUIEvent.OnRadioClick -> {
                // Handled by navigation
            }

            is HomeUIEvent.OnPlaylistClick -> {
                // TODO: Navigate to playlist detail
            }

            is HomeUIEvent.Retry -> {
                loadHomeData()
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            musicRepository.getTopTracks()
                .onSuccess { tracks ->
                    _uiState.value = _uiState.value.copy(topTracks = tracks)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to load top tracks"
                    )
                }

            musicRepository.getTopAlbums()
                .onSuccess { albums ->
                    _uiState.value = _uiState.value.copy(topAlbums = albums)
                }
                .onFailure { exception ->
                }

            musicRepository.getTopArtists()
                .onSuccess { artists ->
                    _uiState.value = _uiState.value.copy(topArtists = artists)
                }
                .onFailure { exception ->
                }

            musicRepository.getGenres()
                .onSuccess { genres ->
                    val filteredGenres = genres.filter { it.id != 0L }
                    _uiState.value = _uiState.value.copy(genres = filteredGenres)
                }
                .onFailure { exception ->
                }

            musicRepository.getRadios()
                .onSuccess { radios ->
                    _uiState.value = _uiState.value.copy(radios = radios)
                }
                .onFailure { exception ->
                }

            musicRepository.getTopPlaylists()
                .onSuccess { playlists ->
                    _uiState.value = _uiState.value.copy(topPlaylists = playlists)
                }
                .onFailure { exception ->
                }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}


