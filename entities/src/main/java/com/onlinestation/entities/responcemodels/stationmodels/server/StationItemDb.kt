package com.onlinestation.entities.responcemodels.stationmodels.server

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class StationItemDb(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "icon")
    val icon: String  ,
    @ColumnInfo(name = "stationUrl")
    val stationUrl: String
)