package com.onlinestation.domain.interactors

import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal

interface FavoriteStationsInteractor {
    suspend fun removeStationDataLocalDB(itemId: Int)
    suspend fun getAllStationDataLocalDB():MutableList<StationItemLocal>
}