package com.onlinestation.domain.utils

import com.onlinestation.data.entities.Constants.Companion.BASE_API_URL
import com.onlinestation.data.entities.Constants.Companion.FOLDER_GENRES
import com.onlinestation.data.entities.Constants.Companion.FOLDER_RADIOS
import com.onlinestation.data.entities.request.GenderItem
import com.onlinestation.data.entities.gendermodels.GenderItemDb
import com.onlinestation.data.entities.gendermodels.ResponseGender
import com.onlinestation.domain.entities.StationItem
import com.onlinestation.data.entities.stationmodels.StationItemDb
import com.onlinestation.data.entities.stationmodels.StationItemResponse

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
    icon = BASE_API_URL + FOLDER_RADIOS + img,
    genre = genre?:"",
    stationUrl = stationUrl?:"",
    isFavorite = isFavorite
)

fun ResponseGender.toDomain() = GenderItem(
    name = name,
    id = id,
    img = BASE_API_URL + FOLDER_GENRES + img
)


