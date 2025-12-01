package com.dilara.beatify.data.mapper

import com.dilara.beatify.data.local.entity.SearchHistoryEntity
import com.dilara.beatify.domain.model.Album
import com.dilara.beatify.domain.model.Artist
import com.dilara.beatify.domain.model.SearchHistory
import com.dilara.beatify.domain.model.Track

fun SearchHistoryEntity.toDomain(): SearchHistory {
    return SearchHistory(
        id = id,
        track = Track(
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
        ),
        searchedAt = searchedAt
    )
}

fun Track.toSearchHistoryEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(
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
        searchedAt = System.currentTimeMillis()
    )
}

