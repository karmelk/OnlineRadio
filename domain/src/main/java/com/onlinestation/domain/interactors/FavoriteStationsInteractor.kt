package com.onlinestation.domain.interactors

import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem

interface FavoriteStationsInteractor {
     fun removeStationDataLocalDB(itemId: Long):MutableList<StationItem>
    suspend fun getAllStationDataLocalDB():MutableList<StationItem>
}