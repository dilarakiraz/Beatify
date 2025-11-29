package com.dilara.beatify.domain.model

data class Playlist(
    val id: Long,
    val name: String,
    val coverUrl: String?,
    val trackCount: Int,
    val createdAt: Long,
    val updatedAt: Long
)


