package com.dilara.beatify.data.mapper

import com.dilara.beatify.data.remote.model.DeezerGenreDto
import com.dilara.beatify.domain.model.Genre

fun DeezerGenreDto.toDomain(): Genre {
    return Genre(
        id = id,
        name = name,
        picture = picture,
        pictureSmall = pictureSmall,
        pictureMedium = pictureMedium,
        pictureBig = pictureBig,
        pictureXl = pictureXl
    )
}