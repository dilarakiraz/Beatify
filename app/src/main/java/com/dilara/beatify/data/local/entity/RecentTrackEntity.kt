package com.dilara.beatify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_tracks")
data class RecentTrackEntity(
    @PrimaryKey
    val trackId: Long,
    val title: String,
    val titleShort: String,
    val duration: Int, // in seconds
    val previewUrl: String?,
    val artistId: Long,
    val artistName: String,
    val albumId: Long,
    val albumTitle: String,
    val albumCover: String?,
    val playedAt: Long = System.currentTimeMillis()
)

