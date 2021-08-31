package com.onlinestation.data.datastore

import com.onlinestation.data.entities.request.QuerySearchBody
import com.onlinestation.data.entities.stationmodels.StationItemResponse
import com.onlinestation.data.entities.Result

interface SearchStationRepository {
    suspend fun searchStationListData( queryBody: QuerySearchBody): Result<List<StationItemResponse>>
}