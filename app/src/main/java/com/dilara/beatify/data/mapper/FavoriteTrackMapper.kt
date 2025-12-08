package com.dilara.beatify.data.mapper

import com.dilara.beatify.data.local.entity.FavoriteTrackEntity
import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.Track

fun FavoriteTrackEntity.toDomain(): Track {
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

fun Track.toFavoriteEntity(position: Int = 0): FavoriteTrackEntity {
    return FavoriteTrackEntity(
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

fun com.dilara.beatify.data.local.entity.RecentTrackEntity.toDomain(): Track {
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

fun Track.toRecentEntity(): com.dilara.beatify.data.local.entity.RecentTrackEntity {
    return com.dilara.beatify.data.local.entity.RecentTrackEntity(
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
        playedAt = System.currentTimeMillis()
    )
}




