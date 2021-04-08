package com.onlinestation.data.datastore

import  com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QuerySearchBody
import com.onlinestation.entities.responcemodels.stationmodels.ResponseStationList
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

interface SearchStationRepository {
    suspend fun searchStationListData( queryBody: QuerySearchBody): Result<List<StationItemResponse>>
}