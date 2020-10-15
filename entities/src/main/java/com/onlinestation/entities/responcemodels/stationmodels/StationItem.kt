package com.onlinestation.entities.responcemodels.stationmodels

import com.squareup.moshi.Json
data class StationItem(
    @Json(name = "id")
    val id: Int,
    @Json(name = "br")
    val br: Int,
    @Json(name = "mt")
    val mt: String,
    @Json(name = "lc")
    val lc: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "genre")
    val genre: String,
    @Json(name = "logo")
    val logo: String,
    @Json(name = "ml")
    val ml: Int
)