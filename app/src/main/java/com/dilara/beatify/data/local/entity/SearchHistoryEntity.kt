package com.dilara.beatify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
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
    val searchedAt: Long = System.currentTimeMillis()
)

