package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.domain.model.Genre
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.presentation.state.GenreDetailUIEvent
import com.dilara.beatify.presentation.state.GenreDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreDetailViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenreDetailUIState())
    val uiState: StateFlow<GenreDetailUIState> = _uiState.asStateFlow()
    
    private var currentGenreId: Long = -1

    fun onEvent(event: GenreDetailUIEvent) {
        when (event) {
            is GenreDetailUIEvent.LoadGenre -> {
                currentGenreId = event.genreId
                loadGenreDetails(event.genreId)
            }
            is GenreDetailUIEvent.OnArtistClick -> {
                // Handled by navigation
            }
            is GenreDetailUIEvent.OnTrackClick -> {
                // Handled by navigation
            }
            is GenreDetailUIEvent.Retry -> {
                if (currentGenreId != -1L) {
                    loadGenreDetails(currentGenreId)
                }
            }
        }
    }

    private fun loadGenreDetails(genreId: Long) {
        viewModelScope.launch {
            if (genreId == 0L) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Bu tür mevcut değil"
                )
                return@launch
            }
            
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val tempGenre = Genre(
                id = genreId,
                name = getGenreName(genreId),
                picture = null,
                pictureSmall = null,
                pictureMedium = null,
                pictureBig = null,
                pictureXl = null
            )
            
            _uiState.value = _uiState.value.copy(genre = tempGenre)

            var hasContent = false
            
            musicRepository.getGenreArtists(genreId, limit = 20)
                .onSuccess { artists ->
                    if (artists.isNotEmpty()) {
                        _uiState.value = _uiState.value.copy(artists = artists)
                        hasContent = true
                    }
                }

            musicRepository.getRadioTracks(genreId, limit = 30)
                .onSuccess { tracks ->
                    if (tracks.isNotEmpty()) {
                        _uiState.value = _uiState.value.copy(radioTracks = tracks)
                        hasContent = true
                    }
                }

            if (!hasContent) {
                _uiState.value = _uiState.value.copy(
                    error = "Bu tür için içerik bulunamadı",
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    private fun getGenreName(genreId: Long): String {
        // Common Deezer genre IDs
        return when (genreId) {
            132L -> "Pop"
            116L -> "Rap/Hip Hop"
            152L -> "Rock"
            113L -> "Dance"
            165L -> "R&B"
            85L -> "Alternative"
            106L -> "Electro"
            466L -> "Folk"
            144L -> "Reggae"
            129L -> "Jazz"
            173L -> "Blues"
            98L -> "Classical"
            97L -> "Country"
            464L -> "Metal"
            169L -> "Soul & Funk"
            2L -> "Films/Games"
            else -> "Genre"
        }
    }
}

