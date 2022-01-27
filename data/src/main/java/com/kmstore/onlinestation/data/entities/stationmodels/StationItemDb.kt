package com.kmstore.onlinestation.data.entities.stationmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class StationItemDb (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "genre")
    val genre: String,
    @ColumnInfo(name = "icon")
    val icon: String  ,
    @ColumnInfo(name = "stationUrl")
    val stationUrl: String
)