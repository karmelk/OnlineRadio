package com.onlinestation.entities.responcemodels.gendermodels

import com.squareup.moshi.Json

data class SecondaryGenreItem(
    @Json(name = "name")
    val name: String,
    @Json(name = "count")
    val count: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "haschildren")
    val haschildren: Boolean,
    @Json(name = "parentid")
    val parentid: Int
)