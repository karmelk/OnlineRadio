package com.onlinestation.domain.interactors

import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.OwnerUserBalance

import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem

interface StationListByGenreIdInteractor {
    suspend fun getStationListByGenreIdData(id: Long): Result<List<StationItem>>
}