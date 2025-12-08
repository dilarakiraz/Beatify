package com.dilara.beatify.presentation.state

import com.dilara.beatify.domain.model.Genre

data class GenresUIState(
    val isLoading: Boolean = false,
    val genres: List<Genre> = emptyList(),
    val error: String? = null
)

sealed class GenresUIEvent {
    data class OnGenreClick(val genreId: Long) : GenresUIEvent()
    data object Retry : GenresUIEvent()
}



