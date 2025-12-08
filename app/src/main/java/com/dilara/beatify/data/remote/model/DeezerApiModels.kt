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
    val artist: DeezerArtistDto? = null
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

// Artist Albums Response
data class DeezerArtistAlbumsResponse(
    @SerializedName("data")
    val albums: List<DeezerAlbumDto>,
    @SerializedName("total")
    val total: Int? = null
)

// Artist Top Tracks Response
data class DeezerArtistTopTracksResponse(
    @SerializedName("data")
    val tracks: List<DeezerTrackDto>
)

// Artist Related Artists Response
data class DeezerRelatedArtistsResponse(
    @SerializedName("data")
    val artists: List<DeezerArtistDto>
)

// Artist Search Response
data class DeezerArtistSearchResponse(
    @SerializedName("data")
    val artists: List<DeezerArtistDto>,
    @SerializedName("total")
    val total: Int
)

// Album Search Response
data class DeezerAlbumSearchResponse(
    @SerializedName("data")
    val albums: List<DeezerAlbumDto>,
    @SerializedName("total")
    val total: Int
)

// Playlist Search Response
data class DeezerPlaylistSearchResponse(
    @SerializedName("data")
    val playlists: List<DeezerPlaylistDto>,
    @SerializedName("total")
    val total: Int
)

// Chart Albums Response
data class DeezerChartAlbumsResponse(
    @SerializedName("data")
    val albums: List<DeezerAlbumDto>
)

// Chart Artists Response
data class DeezerChartArtistsResponse(
    @SerializedName("data")
    val artists: List<DeezerArtistDto>
)

// Genre DTO
data class DeezerGenreDto(
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

// Genres List Response
data class DeezerGenresResponse(
    @SerializedName("data")
    val genres: List<DeezerGenreDto>
)

// Genre Artists Response
data class DeezerGenreArtistsResponse(
    @SerializedName("data")
    val artists: List<DeezerArtistDto>
)

// Genre Radios Response (Radio tracks from genre)
data class DeezerGenreRadioResponse(
    @SerializedName("data")
    val tracks: List<DeezerTrackDto>
)

// Radio DTO
data class DeezerRadioDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
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

// Radios List Response
data class DeezerRadiosResponse(
    @SerializedName("data")
    val radios: List<DeezerRadioDto>
)

// Radio Tracks Response
data class DeezerRadioTracksResponse(
    @SerializedName("data")
    val tracks: List<DeezerTrackDto>
)

// Playlist DTO
data class DeezerPlaylistDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("picture")
    val picture: String?,
    @SerializedName("picture_small")
    val pictureSmall: String?,
    @SerializedName("picture_medium")
    val pictureMedium: String?,
    @SerializedName("picture_big")
    val pictureBig: String?,
    @SerializedName("picture_xl")
    val pictureXl: String?,
    @SerializedName("nb_tracks")
    val trackCount: Int,
    @SerializedName("creator")
    val creator: DeezerPlaylistCreatorDto? = null,
    @SerializedName("creation_date")
    val creationDate: String? = null
)

// Playlist Creator DTO
data class DeezerPlaylistCreatorDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)

// Chart Playlists Response
data class DeezerChartPlaylistsResponse(
    @SerializedName("data")
    val playlists: List<DeezerPlaylistDto>
)

// Playlist Response (single)
data class DeezerPlaylistResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("picture")
    val picture: String?,
    @SerializedName("picture_small")
    val pictureSmall: String?,
    @SerializedName("picture_medium")
    val pictureMedium: String?,
    @SerializedName("picture_big")
    val pictureBig: String?,
    @SerializedName("picture_xl")
    val pictureXl: String?,
    @SerializedName("nb_tracks")
    val trackCount: Int,
    @SerializedName("creator")
    val creator: DeezerPlaylistCreatorDto? = null,
    @SerializedName("creation_date")
    val creationDate: String? = null
)

// Playlist Tracks Response
data class DeezerPlaylistTracksResponse(
    @SerializedName("data")
    val tracks: List<DeezerTrackDto>
)


