package com.onlinestation.domain.entities

data class StationItem(
    val id: Int,
    val name: String,
    val genre: String,
    val icon: String,
    val stationUrl: String,
    val isFavorite: Boolean
) {
    companion object {
        fun emptyItem() = StationItem(-1, "", "", "", "", false)
    }
}