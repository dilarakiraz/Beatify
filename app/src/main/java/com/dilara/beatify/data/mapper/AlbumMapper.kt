package com.dilara.beatify.data.mapper

import com.dilara.beatify.data.remote.model.DeezerAlbumDto
import com.dilara.beatify.data.remote.model.DeezerAlbumResponse
import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist

fun DeezerAlbumDto.toDomain(artist: Artist): Album {
    return Album(
        id = id,
        title = title,
        cover = cover,
        coverSmall = coverSmall,
        coverMedium = coverMedium,
        coverBig = coverBig,
        coverXl = coverXl,
        artist = artist,
        tracks = emptyList()
    )
}

fun DeezerAlbumResponse.toDomain(): Album {
    val domainArtist = artist.toDomain()
    return Album(
        id = id,
        title = title,
        cover = cover,
        coverSmall = coverSmall,
        coverMedium = coverMedium,
        coverBig = coverBig,
        coverXl = coverXl,
        artist = domainArtist,
        tracks = tracks?.tracks?.map { it.toDomain() } ?: emptyList()
    )
}

