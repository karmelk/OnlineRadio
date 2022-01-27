package com.kmstore.onlinestation.data.repository

import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.entities.request.QuerySearchBody
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemResponse

interface SearchStationRepository {
    suspend fun searchStationListData( queryBody: QuerySearchBody): DataResult<List<StationItemResponse>>
}