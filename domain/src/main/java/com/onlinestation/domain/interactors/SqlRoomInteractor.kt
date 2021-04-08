package com.onlinestation.domain.interactors

import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem

interface SqlRoomInteractor {
    suspend fun addRemoveStationFromDB(item: StationItem, stationList: List<StationItem>?): Result<List<StationItem>>
}