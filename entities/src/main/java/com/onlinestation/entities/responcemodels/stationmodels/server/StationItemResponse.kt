package com.onlinestation.entities.responcemodels.stationmodels.server

import com.squareup.moshi.Json

data class StationItemResponse(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "img")
    val img: String,
    @field:Json(name = "link_radio")
    val stationUrl: String
)