package com.dilara.beatify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.presentation.state.GenresUIEvent
import com.dilara.beatify.presentation.state.GenresUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenresViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenresUIState())
    val uiState: StateFlow<GenresUIState> = _uiState.asStateFlow()

    init {
        loadGenres()
    }

    fun onEvent(event: GenresUIEvent) {
        when (event) {
            is GenresUIEvent.OnGenreClick -> {
                // Handled by navigation
            }
            is GenresUIEvent.Retry -> {
                loadGenres()
            }
        }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            musicRepository.getGenres()
                .onSuccess { genres ->
                    _uiState.value = _uiState.value.copy(
                        genres = genres,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to load genres",
                        isLoading = false
                    )
                }
        }
    }
}



