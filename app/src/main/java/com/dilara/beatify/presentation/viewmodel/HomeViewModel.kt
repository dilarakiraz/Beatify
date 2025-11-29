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
        loadTopTracks()
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

            is HomeUIEvent.Retry -> {
                loadTopTracks()
            }
        }
    }

    private fun loadTopTracks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            musicRepository.getTopTracks()
                .onSuccess { tracks ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        topTracks = tracks,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error"
                    )
                }
        }
    }
}


