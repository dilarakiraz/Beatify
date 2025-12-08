package com.dilara.beatify.data.mapper

import com.dilara.beatify.data.local.entity.PlaylistEntity
import com.dilara.beatify.domain.model.Playlist

fun PlaylistEntity.toDomain(trackCount: Int = 0): Playlist {
    return Playlist(
        id = id,
        name = name,
        coverUrl = coverUrl,
        trackCount = trackCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Playlist.toEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = id,
        name = name,
        coverUrl = coverUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}




