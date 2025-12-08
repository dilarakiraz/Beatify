package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Radio
import com.dilara.beatify.domain.model.Track

data class RadioDetailUIState(
    val isLoading: Boolean = false,
    val radio: Radio? = null,
    val tracks: List<Track> = emptyList(),
    val error: String? = null
)

sealed class RadioDetailUIEvent {
    data class LoadRadio(val radioId: Long) : RadioDetailUIEvent()
    data class OnTrackClick(val trackId: Long) : RadioDetailUIEvent()
    data object Retry : RadioDetailUIEvent()
}



