package com.onlinestation.data.entities.stationmodels

import com.squareup.moshi.Json

data class StationItemResponse(
    @field:Json(name = "id")
    val id: Int?=null,
    @field:Json(name = "name")
    val name: String?=null,
    @field:Json(name = "tags")
    val genre: String?=null,
    @field:Json(name = "img")
    val img: String?=null,
    @field:Json(name = "link_radio")
    val stationUrl: String?=null
)