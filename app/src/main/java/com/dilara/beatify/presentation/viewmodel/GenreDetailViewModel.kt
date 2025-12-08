package com.dilara.beatify.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.R
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
    application: Application,
    private val musicRepository: MusicRepository
) : AndroidViewModel(application) {

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
                    error = getApplication<Application>().getString(R.string.error_genre_not_available)
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
                    error = getApplication<Application>().getString(R.string.error_genre_content_not_found),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    private fun getGenreName(genreId: Long): String {
        // Common Deezer genre IDs
        val context = getApplication<Application>()
        return when (genreId) {
            132L -> context.getString(R.string.genre_pop)
            116L -> context.getString(R.string.genre_rap_hiphop)
            152L -> context.getString(R.string.genre_rock)
            113L -> context.getString(R.string.genre_dance)
            165L -> context.getString(R.string.genre_rnb)
            85L -> context.getString(R.string.genre_alternative)
            106L -> context.getString(R.string.genre_electro)
            466L -> context.getString(R.string.genre_folk)
            144L -> context.getString(R.string.genre_reggae)
            129L -> context.getString(R.string.genre_jazz)
            173L -> context.getString(R.string.genre_blues)
            98L -> context.getString(R.string.genre_classical)
            97L -> context.getString(R.string.genre_country)
            464L -> context.getString(R.string.genre_metal)
            169L -> context.getString(R.string.genre_soul_funk)
            2L -> context.getString(R.string.genre_films_games)
            else -> context.getString(R.string.genre)
        }
    }
}

