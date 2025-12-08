package com.dilara.beatify.data.mapper

import com.dilara.beatify.data.remote.model.DeezerPlaylistDto
import com.dilara.beatify.data.remote.model.DeezerPlaylistResponse
import com.dilara.beatify.domain.model.PublicPlaylist

fun DeezerPlaylistDto.toDomain(): PublicPlaylist {
    return PublicPlaylist(
        id = id,
        title = title,
        cover = picture,
        coverSmall = pictureSmall,
        coverMedium = pictureMedium,
        coverBig = pictureBig,
        coverXl = pictureXl,
        trackCount = trackCount,
        creatorName = creator?.name
    )
}

fun DeezerPlaylistResponse.toDomain(): PublicPlaylist {
    return PublicPlaylist(
        id = id,
        title = title,
        cover = picture,
        coverSmall = pictureSmall,
        coverMedium = pictureMedium,
        coverBig = pictureBig,
        coverXl = pictureXl,
        trackCount = trackCount,
        creatorName = creator?.name
    )
}

