package com.onlinestation.entities.responcemodels.gendermodels.server

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "genre")
data class GenderItemDb(
    @PrimaryKey
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "img")
    val img: String
)