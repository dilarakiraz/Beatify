package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.core.utils.Constants
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.presentation.state.SearchUIEvent
import com.dilara.beatify.presentation.state.SearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUIState())
    val uiState: StateFlow<SearchUIState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    init {
        loadSuggestedTracks()
        
        searchQueryFlow
            .debounce(Constants.SEARCH_DEBOUNCE_MS)
            .onEach { query ->
                if (query.isNotBlank()) {
                    performSearch(query)
                } else {
                    _uiState.value = _uiState.value.copy(
                        tracks = emptyList(),
                        artists = emptyList(),
                        error = null,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadSuggestedTracks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingSuggestions = true)
            
            musicRepository.getTopTracks()
                .onSuccess { tracks ->
                    // Rastgele 8-10 şarkı seç (öneri olarak)
                    val shuffled = tracks.shuffled()
                    val suggested = shuffled.take(minOf(10, shuffled.size))
                    
                    _uiState.value = _uiState.value.copy(
                        suggestedTracks = suggested,
                        isLoadingSuggestions = false
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoadingSuggestions = false
                    )
                }
        }
    }

    fun onEvent(event: SearchUIEvent) {
        when (event) {
            is SearchUIEvent.OnQueryChange -> {
                val query = event.query
                _uiState.value = _uiState.value.copy(
                    searchQuery = query,
                    error = null
                )
                searchQueryFlow.value = query
            }
            
            is SearchUIEvent.OnTrackClick -> {
                // TODO: Navigate to track detail or play track
            }
            
            is SearchUIEvent.OnArtistClick -> {
                // TODO: Navigate to artist detail
            }
            
            is SearchUIEvent.OnAlbumClick -> {
                // TODO: Navigate to album detail
            }
            
            is SearchUIEvent.OnSearchFocusChanged -> {
                _uiState.value = _uiState.value.copy(
                    isSearchActive = !_uiState.value.isSearchActive
                )
            }
            
            is SearchUIEvent.ClearSearch -> {
                // Suggested tracks'i koru, sadece search state'i temizle
                _uiState.value = _uiState.value.copy(
                    searchQuery = "",
                    tracks = emptyList(),
                    artists = emptyList(),
                    error = null,
                    isLoading = false
                )
                searchQueryFlow.value = ""
            }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            val tracksResult = musicRepository.searchTracks(query, limit = 25)
            val artistsResult = musicRepository.searchArtists(query, limit = 10)

            tracksResult.fold(
                onSuccess = { tracks ->
                    artistsResult.fold(
                        onSuccess = { artists ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                tracks = tracks,
                                artists = artists,
                                error = null
                            )
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                tracks = tracks,
                                artists = emptyList(),
                                error = null // Don't fail completely if artists search fails
                            )
                        }
                    )
                },
                onFailure = { exception ->
                    artistsResult.fold(
                        onSuccess = { artists ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                tracks = emptyList(),
                                artists = artists,
                                error = null // Don't fail completely if tracks search fails
                            )
                        },
                        onFailure = { artistsException ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = exception.message ?: "Arama başarısız",
                                tracks = emptyList(),
                                artists = emptyList()
                            )
                        }
                    )
                }
            )
        }
    }
}

