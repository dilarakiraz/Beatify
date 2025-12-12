package com.dilara.beatify.domain.model

/**
 * Domain model for radio station
 */
data class Radio(
    val id: Long,
    val title: String,
    val picture: String?,
    val pictureSmall: String?,
    val pictureMedium: String?,
    val pictureBig: String?,
    val pictureXl: String?
)