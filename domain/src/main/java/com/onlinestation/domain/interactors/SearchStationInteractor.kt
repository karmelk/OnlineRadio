package com.onlinestation.domain.interactors

import com.onlinestation.entities.responcemodels.OwnerUserBalance
import kotlinx.coroutines.flow.Flow
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

interface SearchStationInteractor {
    suspend fun searchStationListData(searchKeyword: String) : Result<List<StationItem>>
}