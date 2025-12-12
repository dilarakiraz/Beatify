package com.dilara.beatify.domain.model

/**
 * Domain model for music genre
 */
data class Genre(
    val id: Long,
    val name: String,
    val picture: String?,
    val pictureSmall: String?,
    val pictureMedium: String?,
    val pictureBig: String?,
    val pictureXl: String?
)