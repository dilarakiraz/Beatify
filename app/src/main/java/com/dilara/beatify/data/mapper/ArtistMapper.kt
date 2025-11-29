package com.dilara.beatify.data.mapper

import com.dilara.beatify.data.remote.model.DeezerArtistDto
import com.dilara.beatify.data.remote.model.DeezerArtistResponse
import com.dilara.beatify.domain.model.Artist

fun DeezerArtistDto.toDomain(): Artist {
    return Artist(
        id = id,
        name = name,
        picture = picture,
        pictureSmall = pictureSmall,
        pictureMedium = pictureMedium,
        pictureBig = pictureBig,
        pictureXl = pictureXl
    )
}

fun DeezerArtistResponse.toDomain(): Artist {
    return Artist(
        id = id,
        name = name,
        picture = picture,
        pictureSmall = pictureSmall,
        pictureMedium = pictureMedium,
        pictureBig = pictureBig,
        pictureXl = pictureXl
    )
}


