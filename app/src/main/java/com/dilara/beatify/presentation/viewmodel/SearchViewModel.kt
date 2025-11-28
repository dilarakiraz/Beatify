package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.core.utils.Constants
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.presentation.state.SearchUIEvent
import com.dilara.beatify.presentation.state.SearchUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
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
        searchQueryFlow
            .debounce(Constants.SEARCH_DEBOUNCE_MS)
            .onEach { query ->
                if (query.isNotBlank()) {
                    performSearch(query)
                } else {
                    _uiState.value = _uiState.value.copy(
                        tracks = emptyList(),
                        error = null,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
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
                _uiState.value = SearchUIState()
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

            musicRepository.searchTracks(query, limit = 25)
                .onSuccess { tracks ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tracks = tracks,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Search failed",
                        tracks = emptyList()
                    )
                }
        }
    }
}

