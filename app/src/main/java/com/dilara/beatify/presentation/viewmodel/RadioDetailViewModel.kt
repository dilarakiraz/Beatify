package com.dilara.beatify.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dilara.beatify.R
import com.dilara.beatify.domain.repository.MusicRepository
import com.dilara.beatify.presentation.state.RadioDetailUIEvent
import com.dilara.beatify.presentation.state.RadioDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RadioDetailViewModel @Inject constructor(
    application: Application,
    private val musicRepository: MusicRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RadioDetailUIState())
    val uiState: StateFlow<RadioDetailUIState> = _uiState.asStateFlow()
    
    private var currentRadioId: Long = -1

    fun onEvent(event: RadioDetailUIEvent) {
        when (event) {
            is RadioDetailUIEvent.LoadRadio -> {
                // Eğer farklı bir radio'ya geçiliyorsa state'i resetle
                if (currentRadioId != event.radioId) {
                    _uiState.value = RadioDetailUIState()
                }
                currentRadioId = event.radioId
                
                if (event.initialRadioTitle != null) {
                    _uiState.value = RadioDetailUIState(
                        isLoading = true,
                        radio = com.dilara.beatify.domain.model.Radio(
                            id = event.radioId,
                            title = event.initialRadioTitle,
                            picture = null,
                            pictureSmall = null,
                            pictureMedium = null,
                            pictureBig = null,
                            pictureXl = null
                        )
                    )
                }
                loadRadioDetails(event.radioId, event.initialRadioTitle)
            }
            is RadioDetailUIEvent.OnTrackClick -> {
                // Handled by navigation
            }
            is RadioDetailUIEvent.Retry -> {
                if (currentRadioId != -1L) {
                    loadRadioDetails(currentRadioId, null)
                }
            }
        }
    }

    private fun loadRadioDetails(radioId: Long, initialRadioTitle: String?) {
        viewModelScope.launch {
            // Radio bilgisi zaten varsa (initialRadioTitle'dan), sadece tracks için loading yap
            _uiState.value = _uiState.value.copy(
                isLoading = _uiState.value.radio == null,
                error = null
            )

            // Radio bilgisini ve tracks'i paralel yükle
            val radioDeferred = async {
                musicRepository.getRadios()
                    .getOrNull()
                    ?.find { it.id == radioId }
            }
            
            val tracksResult = musicRepository.getRadioTracks(radioId)
            
            // Radio bilgisini güncelle (sadece API'den geldiyse, initialRadioTitle varsa zaten state'te)
            radioDeferred.await()?.let { radio ->
                _uiState.value = _uiState.value.copy(radio = radio)
            }

            tracksResult.onSuccess { tracks ->
                _uiState.value = _uiState.value.copy(
                    tracks = tracks,
                    isLoading = false
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: getApplication<Application>().getString(R.string.error_radio_load_failed)
                )
            }
        }
    }
}

