package com.onlinestation.entities.responcemodels.gendermodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
@Entity(tableName = "primaryGenre")
data class PrimaryGenreItem(
    @Json(name = "name")
    val name: String,
    @Json(name = "count")
    val count: Int,
    @PrimaryKey
    @Json(name = "id")
    val id: Int,
    @Json(name = "haschildren")
    val haschildren: Boolean,
    @Json(name = "parentid")
    val parentid: Int
)