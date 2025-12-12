package com.dilara.beatify.data.mapper

import com.dilara.beatify.data.local.entity.PlaylistTrackEntity
import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track

fun PlaylistTrackEntity.toDomain(): Track {
    return Track(
        id = trackId,
        title = title,
        titleShort = titleShort,
        duration = duration,
        previewUrl = previewUrl,
        artist = Artist(
            id = artistId,
            name = artistName,
            picture = null,
            pictureSmall = null,
            pictureMedium = null,
            pictureBig = null,
            pictureXl = null
        ),
        album = Album(
            id = albumId,
            title = albumTitle,
            cover = albumCover,
            coverSmall = null,
            coverMedium = null,
            coverBig = albumCover,
            coverXl = null,
            artist = Artist(
                id = artistId,
                name = artistName,
                picture = null,
                pictureSmall = null,
                pictureMedium = null,
                pictureBig = null,
                pictureXl = null
            ),
            tracks = emptyList()
        )
    )
}

fun Track.toPlaylistTrackEntity(playlistId: Long, position: Int): PlaylistTrackEntity {
    return PlaylistTrackEntity(
        playlistId = playlistId,
        trackId = id,
        title = title,
        titleShort = titleShort,
        duration = duration,
        previewUrl = previewUrl,
        artistId = artist.id,
        artistName = artist.name,
        albumId = album.id,
        albumTitle = album.title,
        albumCover = album.coverBig ?: album.cover,
        position = position,
        addedAt = System.currentTimeMillis()
    )
}