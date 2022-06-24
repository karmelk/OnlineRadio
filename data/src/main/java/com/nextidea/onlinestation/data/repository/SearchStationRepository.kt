package com.nextidea.onlinestation.data.repository

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.request.QuerySearchBody
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemResponse

interface SearchStationRepository {
    suspend fun searchStationListData( queryBody: QuerySearchBody): DataResult<List<StationItemResponse>>
}