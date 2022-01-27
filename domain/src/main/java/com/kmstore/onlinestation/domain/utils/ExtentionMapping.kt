package com.kmstore.onlinestation.domain.utils

import com.kmstore.onlinestation.data.BuildConfig
import com.kmstore.onlinestation.data.entities.Constants.Companion.FOLDER_GENRES
import com.kmstore.onlinestation.data.entities.Constants.Companion.FOLDER_RADIOS
import com.kmstore.onlinestation.data.entities.request.GenderItem
import com.kmstore.onlinestation.data.entities.gendermodels.ResponseGender
import com.kmstore.onlinestation.domain.entities.StationItem
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemDb
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemResponse

fun StationItemDb.toDomain(
    isFavorite: Boolean
) = StationItem(
    id = id,
    name = name,
    icon = icon,
    genre = genre,
    stationUrl = stationUrl,
    isFavorite = isFavorite
)

fun StationItem.fromStationToStationDB() = StationItemDb(
    id = id,
    name = name,
    icon = icon,
    genre = genre,
    stationUrl = stationUrl
)

fun StationItemResponse.toDomain(
    isFavorite: Boolean
) = StationItem(
    id = id?:0,
    name = name?:"",
    icon = BuildConfig.API_URL + FOLDER_RADIOS + img,
    genre = genre?:"",
    stationUrl = stationUrl?:"",
    isFavorite = isFavorite
)

fun ResponseGender.toDomain() = GenderItem(
    name = name,
    id = id,
    img =BuildConfig.API_URL + FOLDER_GENRES + img
)


