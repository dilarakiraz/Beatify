package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.domain.repository.RecentTracksRepository
import com.dilara.beatify.presentation.state.HomeUIEvent
import com.dilara.beatify.presentation.state.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val recentTracksRepository: RecentTracksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        loadHomeData()
        observeRecentTracks()
    }
    
    private fun observeRecentTracks() {
        recentTracksRepository.getRecentTracks(limit = 20)
            .onEach { tracks ->
                _uiState.value = _uiState.value.copy(recentTracks = tracks)
                // Daily Mix oluştur
                if (tracks.isNotEmpty()) {
                    generateDailyMix(tracks)
                }
            }
            .launchIn(viewModelScope)
    }
    
    private fun generateDailyMix(recentTracks: List<com.dilara.beatify.domain.model.Track>) {
        viewModelScope.launch {
            val allTracks = mutableListOf<com.dilara.beatify.domain.model.Track>()
            
            // Recently Played tracks'lerden unique artist'leri al
            val uniqueArtists = recentTracks.map { it.artist }.distinctBy { it.id }
            
            // Her artist'in top tracks'lerini al
            uniqueArtists.take(5).forEach { artist ->
                musicRepository.getArtistTopTracks(artist.id, limit = 5)
                    .onSuccess { artistTracks ->
                        allTracks.addAll(artistTracks)
                    }
                
                // Related artists'lerin top tracks'lerini de ekle
                musicRepository.getRelatedArtists(artist.id, limit = 3)
                    .onSuccess { relatedArtists ->
                        relatedArtists.take(2).forEach { relatedArtist ->
                            musicRepository.getArtistTopTracks(relatedArtist.id, limit = 3)
                                .onSuccess { relatedTracks ->
                                    allTracks.addAll(relatedTracks)
                                }
                        }
                    }
            }
            
            // Duplicate'leri kaldır, karıştır ve ilk 40'ı al
            if (allTracks.isNotEmpty()) {
                val uniqueTracks = allTracks
                    .distinctBy { it.id }
                    .shuffled()
                    .take(40)
                
                _uiState.value = _uiState.value.copy(dailyMix = uniqueTracks)
            }
        }
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


