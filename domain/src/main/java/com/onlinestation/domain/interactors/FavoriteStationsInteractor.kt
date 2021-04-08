package com.onlinestation.domain.interactors

import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem

interface FavoriteStationsInteractor {
    suspend fun removeStationDataLocalDB(item: StationItem):Result<List<StationItem>>
    suspend fun getAllStationDataLocalDB():List<StationItem>
}