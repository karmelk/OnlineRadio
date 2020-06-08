package com.onlinestation.entities.responcemodels.stationmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "radioStation")
data class StationItemLocal(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @field:Json(name = "id")
    val id: Int,
    @ColumnInfo(name = "br")
    @field:Json(name = "br")
    val br: Int?,
    @ColumnInfo(name = "mt")
    @field:Json(name = "mt")
    val mt: String?,
    @ColumnInfo(name = "lc")
    @field:Json(name = "lc")
    val lc: Int?,
    @ColumnInfo(name = "name")
    @field:Json(name = "name")
    val name: String?,
    @ColumnInfo(name = "genre")
    @field:Json(name = "genre")
    val genre: String?,
    @ColumnInfo(name = "logo")
    @field:Json(name = "logo")
    val logo: String?,
    @ColumnInfo(name = "ml")
    @field:Json(name = "ml")
    val ml: Int?,
    @ColumnInfo(name = "createDateTime")
    @field:Json(name = "createDateTime")
    var createDateTime: Long,
    @ColumnInfo(name = "isFavorite")
    @field:Json(name = "isFavorite")
    var isFavorite: Boolean


)