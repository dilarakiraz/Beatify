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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
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
            
            is FavoritesUIEvent.ReorderFavorites -> {
                reorderFavorites(event.fromIndex, event.toIndex)
            }
        }
    }
    
    private fun loadFavorites() {
        favoritesRepository.getAllFavorites()
            .onStart { _uiState.value = _uiState.value.copy(isLoading = true, error = null) }
            .catch { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Favoriler yüklenemedi"
                )
            }
            .onEach { tracks ->
                _uiState.value = _uiState.value.copy(
                    favoriteTracks = tracks,
                    favoriteTrackIds = tracks.map { it.id }.toSet(),
                    isLoading = false,
                    error = null
                )
            }
            .launchIn(viewModelScope)
    }
    
    private fun toggleFavorite(track: Track) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(track).fold(
                onSuccess = { /* Automatically updated via Flow */ },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Favori durumu değiştirilemedi"
                    )
                }
            )
        }
    }
    
    private fun reorderFavorites(fromIndex: Int, toIndex: Int) {
        // Optimistic UI update
        val currentTracks = _uiState.value.favoriteTracks.toMutableList()
        if (fromIndex in currentTracks.indices && toIndex in currentTracks.indices) {
            val movedTrack = currentTracks.removeAt(fromIndex)
            currentTracks.add(toIndex, movedTrack)
            _uiState.value = _uiState.value.copy(favoriteTracks = currentTracks)
        }
        
        viewModelScope.launch {
            favoritesRepository.reorderFavorites(fromIndex, toIndex).fold(
                onSuccess = { /* Already updated optimistically */ },
                onFailure = { exception ->
                    loadFavorites()
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Sıralama değiştirilemedi"
                    )
                }
            )
        }
    }
    
    fun isFavorite(trackId: Long): kotlinx.coroutines.flow.Flow<Boolean> {
        return favoritesRepository.isFavorite(trackId)
    }
}
