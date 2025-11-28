package com.dilara.beatify.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Deezer API Response Models
 */

// Chart Response
data class DeezerChartResponse(
    @SerializedName("data")
    val tracks: List<DeezerTrackDto>
)

// Search Response
data class DeezerSearchResponse(
    @SerializedName("data")
    val tracks: List<DeezerTrackDto>,
    @SerializedName("total")
    val total: Int
)

// Track DTO
data class DeezerTrackDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_short")
    val titleShort: String,
    @SerializedName("duration")
    val duration: Int, // in seconds
    @SerializedName("preview")
    val previewUrl: String?,
    @SerializedName("artist")
    val artist: DeezerArtistDto,
    @SerializedName("album")
    val album: DeezerAlbumDto
)

// Track Response (single)
data class DeezerTrackResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_short")
    val titleShort: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("preview")
    val previewUrl: String?,
    @SerializedName("artist")
    val artist: DeezerArtistDto,
    @SerializedName("album")
    val album: DeezerAlbumDto
)

// Artist DTO
data class DeezerArtistDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val picture: String?,
    @SerializedName("picture_small")
    val pictureSmall: String?,
    @SerializedName("picture_medium")
    val pictureMedium: String?,
    @SerializedName("picture_big")
    val pictureBig: String?,
    @SerializedName("picture_xl")
    val pictureXl: String?
)

// Artist Response (single)
data class DeezerArtistResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val picture: String?,
    @SerializedName("picture_small")
    val pictureSmall: String?,
    @SerializedName("picture_medium")
    val pictureMedium: String?,
    @SerializedName("picture_big")
    val pictureBig: String?,
    @SerializedName("picture_xl")
    val pictureXl: String?
)

// Album DTO
data class DeezerAlbumDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("cover")
    val cover: String?,
    @SerializedName("cover_small")
    val coverSmall: String?,
    @SerializedName("cover_medium")
    val coverMedium: String?,
    @SerializedName("cover_big")
    val coverBig: String?,
    @SerializedName("cover_xl")
    val coverXl: String?,
    @SerializedName("artist")
    val artist: DeezerArtistDto
)

// Album Response (single)
data class DeezerAlbumResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("cover")
    val cover: String?,
    @SerializedName("cover_small")
    val coverSmall: String?,
    @SerializedName("cover_medium")
    val coverMedium: String?,
    @SerializedName("cover_big")
    val coverBig: String?,
    @SerializedName("cover_xl")
    val coverXl: String?,
    @SerializedName("artist")
    val artist: DeezerArtistDto,
    @SerializedName("tracks")
    val tracks: DeezerAlbumTracksResponse?
)

// Album Tracks Response
data class DeezerAlbumTracksResponse(
    @SerializedName("data")
    val tracks: List<DeezerTrackDto>
)

