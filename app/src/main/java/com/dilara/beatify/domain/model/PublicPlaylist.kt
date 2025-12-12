package com.dilara.beatify.domain.model

/**
 * Public playlist from Deezer API
 * Different from local Playlist model which is for user's own playlists
 */
data class PublicPlaylist(
    val id: Long,
    val title: String,
    val cover: String?,
    val coverSmall: String?,
    val coverMedium: String?,
    val coverBig: String?,
    val coverXl: String?,
    val trackCount: Int,
    val creatorName: String? = null
)