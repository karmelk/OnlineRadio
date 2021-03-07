package com.onlinestation.domain.utils

import com.kmworks.appbase.utils.Constants.Companion.BASE_OWNER_SERVER_API_URL
import com.kmworks.appbase.utils.Constants.Companion.FOLDER_GENRES
import com.kmworks.appbase.utils.Constants.Companion.FOLDER_RADIOS
import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.entities.responcemodels.gendermodels.server.GenderItemDb
import com.onlinestation.entities.responcemodels.gendermodels.server.ResponseGender
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemDb
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

fun StationItemDb.fromDBStationToStation(
    isFavorite: Boolean
) = StationItem(
    id = id,
    name = name,
    icon = BASE_OWNER_SERVER_API_URL + FOLDER_RADIOS + icon,
    stationUrl = stationUrl,
    isFavorite = isFavorite
)
fun StationItem.fromStationToStationDB() = StationItemDb(
    id = id,
    name = name,
    icon = BASE_OWNER_SERVER_API_URL + FOLDER_RADIOS + icon,
    stationUrl = stationUrl
)
fun StationItemResponse.fromDBStationToStation(
    isFavorite: Boolean
) = StationItem(
    id = id,
    name = name,
    icon = BASE_OWNER_SERVER_API_URL + FOLDER_RADIOS + img,
    stationUrl = stationUrl,
    isFavorite = isFavorite
)

fun ResponseGender.toGenreItem() = GenderItem(
    name = name,
    id = id,
    img = BASE_OWNER_SERVER_API_URL + FOLDER_GENRES + img
)

fun GenderItemDb.fromGenreItemDbToGenreItem() = GenderItem(
    name = name,
    id = id,
    img = img
)

fun ResponseGender.toGenreItemDb() = GenderItemDb(
    name = name,
    id = id,
    img = img
)


