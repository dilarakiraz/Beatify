package com.dilara.beatify.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.R
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.presentation.state.AlbumDetailUIEvent
import com.dilara.beatify.presentation.state.AlbumDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    application: Application,
    private val musicRepository: MusicRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(AlbumDetailUIState())
    val uiState: StateFlow<AlbumDetailUIState> = _uiState.asStateFlow()

    fun onEvent(event: AlbumDetailUIEvent) {
        when (event) {
            is AlbumDetailUIEvent.LoadAlbum -> {
                loadAlbumDetails(event.albumId)
            }
            is AlbumDetailUIEvent.Refresh -> {
                loadAlbumDetails(event.albumId, isRefresh = true)
            }
        }
    }

    private fun loadAlbumDetails(albumId: Long, isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = !isRefresh, error = null) }

            musicRepository.getAlbumById(albumId).fold(
                onSuccess = { album ->
                    _uiState.update { 
                        it.copy(
                            album = album,
                            tracks = album.tracks,
                            isLoading = false
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            error = exception.message ?: getApplication<Application>().getString(R.string.error_album_load_failed),
                            isLoading = false
                        )
                    }
                }
            )
        }
    }
}

