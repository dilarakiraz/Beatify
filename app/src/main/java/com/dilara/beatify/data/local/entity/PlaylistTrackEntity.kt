package com.dilara.beatify.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist_tracks",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("playlistId"), Index("trackId")]
)
data class PlaylistTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val playlistId: Long,
    val trackId: Long,
    val title: String,
    val titleShort: String,
    val duration: Int,
    val previewUrl: String?,
    val artistId: Long,
    val artistName: String,
    val albumId: Long,
    val albumTitle: String,
    val albumCover: String?,
    val position: Int,
    val addedAt: Long = System.currentTimeMillis()
)

