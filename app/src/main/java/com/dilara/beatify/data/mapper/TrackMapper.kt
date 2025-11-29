package com.dilara.beatify.data.mapper

import com.dilara.beatify.data.remote.model.DeezerTrackDto
import com.dilara.beatify.data.remote.model.DeezerTrackResponse
import com.dilara.beatify.domain.model.Track

fun DeezerTrackDto.toDomain(): Track {
    return Track(
        id = id,
        title = title,
        titleShort = titleShort,
        duration = duration,
        previewUrl = previewUrl,
        artist = artist.toDomain(),
        album = album.toDomain(artist = artist.toDomain())
    )
}

fun DeezerTrackResponse.toDomain(): Track {
    return Track(
        id = id,
        title = title,
        titleShort = titleShort,
        duration = duration,
        previewUrl = previewUrl,
        artist = artist.toDomain(),
        album = album.toDomain(artist = artist.toDomain())
    )
}


