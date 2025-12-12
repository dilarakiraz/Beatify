package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Radio

data class RadiosUIState(
    val isLoading: Boolean = false,
    val radios: List<Radio> = emptyList(),
    val error: String? = null
)

sealed class RadiosUIEvent {
    data class OnRadioClick(val radioId: Long) : RadiosUIEvent()
    data object Retry : RadiosUIEvent()
}