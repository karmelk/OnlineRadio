package com.nextidea.onlinestation.data.entities.gendermodels


import com.nextidea.onlinestation.data.entities.request.GenderItem
import com.squareup.moshi.Json

data class ResponseGender(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "iso_3166_1")
    val isoCode: String,
    @field:Json(name = "stationcount")
    val stationcount: Int
){
    companion object {
        fun ResponseGender.toDomain() = GenderItem(
            name = name,
            stationcount = stationcount,
            isoCode = isoCode
        )
    }
}