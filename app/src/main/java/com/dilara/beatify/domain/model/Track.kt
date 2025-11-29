package com.dilara.beatify.domain.model

data class Track(
    val id: Long,
    val title: String,
    val titleShort: String,
    val duration: Int, // in seconds
    val previewUrl: String?,
    val artist: Artist,
    val album: Album
)


