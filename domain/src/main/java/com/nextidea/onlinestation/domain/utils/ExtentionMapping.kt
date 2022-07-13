package com.nextidea.onlinestation.domain.utils

import com.nextidea.onlinestation.data.BuildConfig
import com.nextidea.onlinestation.data.entities.Constants.Companion.FOLDER_GENRES
import com.nextidea.onlinestation.data.entities.Constants.Companion.FOLDER_RADIOS
import com.nextidea.onlinestation.data.entities.request.GenderItem
import com.nextidea.onlinestation.data.entities.gendermodels.ResponseGender
import com.nextidea.onlinestation.domain.entities.StationItem
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemDb
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemResponse

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


