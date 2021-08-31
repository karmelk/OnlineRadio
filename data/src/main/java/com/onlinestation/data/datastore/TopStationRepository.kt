package com.onlinestation.data.datastore

import com.onlinestation.data.entities.request.QueryTopStationBody
import com.onlinestation.data.entities.stationmodels.StationItemResponse
import com.onlinestation.data.entities.Result

interface TopStationRepository {

    suspend fun getTopStations(queryBody: QueryTopStationBody): Result<List<StationItemResponse>>
}