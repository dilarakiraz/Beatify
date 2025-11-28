package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.domain.model.Track
import com.dilara.beatify.domain.repository.FavoritesRepository
import com.dilara.beatify.presentation.state.FavoritesUIEvent
import com.dilara.beatify.presentation.state.FavoritesUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FavoritesUIState())
    val uiState: StateFlow<FavoritesUIState> = _uiState.asStateFlow()
    
    init {
        loadFavorites()
    }
    
    fun onEvent(event: FavoritesUIEvent) {
        when (event) {
            is FavoritesUIEvent.OnTrackClick -> {
                // TODO: Navigate to track detail or play track
            }
            
            is FavoritesUIEvent.ToggleFavorite -> {
                toggleFavorite(event.track)
            }
            
            is FavoritesUIEvent.LoadFavorites -> {
                loadFavorites()
            }
        }
    }
    
    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            favoritesRepository.getAllFavorites()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load favorites"
                    )
                }
                .collect { tracks ->
                    _uiState.value = _uiState.value.copy(
                        favoriteTracks = tracks,
                        favoriteTrackIds = tracks.map { it.id }.toSet(),
                        isLoading = false,
                        error = null
                    )
                }
        }
    }
    
    private fun toggleFavorite(track: Track) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(track).fold(
                onSuccess = { },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to toggle favorite"
                    )
                }
            )
        }
    }
    
    fun isFavorite(trackId: Long): kotlinx.coroutines.flow.Flow<Boolean> {
        return favoritesRepository.isFavorite(trackId)
    }
}

