package com.onlinestation.domain.interactors

import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

interface TopStationInteractor {
    suspend fun getTopStationList(): Result<List<StationItem>>
}