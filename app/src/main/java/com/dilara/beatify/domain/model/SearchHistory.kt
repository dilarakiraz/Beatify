package com.dilara.beatify.domain.model

data class SearchHistory(
    val id: Long,
    val track: Track,
    val searchedAt: Long
)

