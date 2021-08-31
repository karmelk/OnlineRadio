package com.onlinestation.data.entities.gendermodels


import com.squareup.moshi.Json

data class ResponseGender(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "img")
    val img: String
)