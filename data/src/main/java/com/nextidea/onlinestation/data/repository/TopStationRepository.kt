package com.nextidea.onlinestation.data.repository

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.request.QueryTopStationBody
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemResponse

interface TopStationRepository {

    suspend fun getTopStations(queryBody: QueryTopStationBody): DataResult<List<StationItemResponse>>
}