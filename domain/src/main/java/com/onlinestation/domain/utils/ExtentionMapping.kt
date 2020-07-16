package com.onlinestation.domain.utils

import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.entities.responcemodels.gendermodels.PrimaryGenreItem
import com.onlinestation.entities.responcemodels.stationmodels.StationItem
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal

fun StationItem.toLocalStation(createDateTime: Long, isFavorite: Boolean) = StationItemLocal(
    id = id,
    br = br,
    mt = mt,
    lc = lc,
    name = name,
    genre = genre,
    logo = logo,
    ml = ml,
    createDateTime = createDateTime,
    isFavorite = isFavorite
)

fun PrimaryGenreItem.toGenreItem() = GenderItem(
    genderName = name,
    selectedItem = false
)


