package com.onlinestation.entities.responcemodels.stationmodels

import com.squareup.moshi.Json

data class ResponseStationTunein(
    @field:Json(name = "base-m3u")
    val baseM3u: String?,
    @field:Json(name = "base-xspf")
    val baseXspf: String?,
    @field:Json(name = "base")
    val base: String?
)