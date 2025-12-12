package com.dilara.beatify.data.mapper

import com.dilara.beatify.data.remote.model.DeezerRadioDto
import com.dilara.beatify.domain.model.Radio

fun DeezerRadioDto.toDomain(): Radio {
    return Radio(
        id = id,
        title = title,
        picture = picture,
        pictureSmall = pictureSmall,
        pictureMedium = pictureMedium,
        pictureBig = pictureBig,
        pictureXl = pictureXl
    )
}