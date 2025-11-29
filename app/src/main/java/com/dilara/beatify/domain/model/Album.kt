package com.dilara.beatify.domain.model

data class Album(
    val id: Long,
    val title: String,
    val cover: String?,
    val coverSmall: String?,
    val coverMedium: String?,
    val coverBig: String?,
    val coverXl: String?,
    val artist: Artist,
    val tracks: List<Track> = emptyList()
)


