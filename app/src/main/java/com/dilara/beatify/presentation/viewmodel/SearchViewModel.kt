package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.core.utils.Constants
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.domain.repository.SearchHistoryRepository
import com.dilara.beatify.presentation.state.SearchUIEvent
import com.dilara.beatify.presentation.state.SearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUIState())
    val uiState: StateFlow<SearchUIState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    init {
        loadSuggestedTracks()
        loadSearchHistory()

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

    private fun loadSearchHistory() {
        searchHistoryRepository.getRecentSearches(limit = 10)
            .catch { exception ->
                exception.printStackTrace()
            }
            .onEach { searchHistory ->
                _uiState.value = _uiState.value.copy(
                    searchHistory = searchHistory
                )
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: SearchUIEvent) {
        when (event) {
            is SearchUIEvent.OnQueryChange -> {
                updateSearchQuery(event.query)
            }

            is SearchUIEvent.OnTrackClick -> {
                val track = findTrackById(event.trackId)
                track?.let {
                    saveToHistory(it)
                }
                // TODO: Navigate to track detail or play track
            }

            is SearchUIEvent.OnArtistClick -> {
                // TODO: Navigate to artist detail
            }

            is SearchUIEvent.OnAlbumClick -> {
                // TODO: Navigate to album detail
            }

            is SearchUIEvent.OnSearchHistoryClick -> {
                updateSearchQuery(event.query)
            }

            is SearchUIEvent.OnSearchFocusChanged -> {
                _uiState.value = _uiState.value.copy(
                    isSearchActive = !_uiState.value.isSearchActive
                )
            }

            is SearchUIEvent.ClearSearch -> {
                _uiState.value = _uiState.value.copy(
                    searchQuery = "",
                    tracks = emptyList(),
                    artists = emptyList(),
                    error = null,
                    isLoading = false
                )
                searchQueryFlow.value = ""
            }

            is SearchUIEvent.DeleteSearchHistory -> {
                deleteSearchHistory(event.searchHistoryId)
            }

            else -> {}
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

            val tracks = tracksResult.getOrNull() ?: emptyList()
            val artists = artistsResult.getOrNull() ?: emptyList()
            val error = when {
                tracksResult.isFailure && artistsResult.isFailure -> 
                    tracksResult.exceptionOrNull()?.message ?: "Arama başarısız"
                else -> null
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                tracks = tracks,
                artists = artists,
                error = error
            )
        }
    }

    private fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            error = null
        )
        searchQueryFlow.value = query
    }

    private fun saveToHistory(track: com.dilara.beatify.domain.model.Track) {
        viewModelScope.launch {
            searchHistoryRepository.addSearch(track).fold(
                onSuccess = { },
                onFailure = { }
            )
        }
    }

    private fun deleteSearchHistory(id: Long) {
        viewModelScope.launch {
            searchHistoryRepository.deleteSearch(id).fold(
                onSuccess = { },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Arama geçmişi silinemedi"
                    )
                }
            )
        }
    }
    
    private fun findTrackById(trackId: Long): com.dilara.beatify.domain.model.Track? {
        val currentState = _uiState.value
        return currentState.tracks.find { it.id == trackId }
            ?: currentState.suggestedTracks.find { it.id == trackId }
            ?: currentState.searchHistory.find { it.track.id == trackId }?.track
    }
}

