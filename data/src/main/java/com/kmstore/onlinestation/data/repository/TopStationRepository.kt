package com.kmstore.onlinestation.data.repository

import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.entities.request.QueryTopStationBody
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemResponse

interface TopStationRepository {

    suspend fun getTopStations(queryBody: QueryTopStationBody): DataResult<List<StationItemResponse>>
}