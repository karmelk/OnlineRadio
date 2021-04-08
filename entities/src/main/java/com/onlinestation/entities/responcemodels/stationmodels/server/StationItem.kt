package com.onlinestation.entities.responcemodels.stationmodels.server

data class StationItem(
    val id: Int,
    val name: String,
    val icon: String,
    val stationUrl: String,
    val isFavorite: Boolean
)