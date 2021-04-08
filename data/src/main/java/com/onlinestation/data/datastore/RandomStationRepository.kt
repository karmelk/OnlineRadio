package com.onlinestation.data.datastore

import  com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryRandomStationBody
import com.onlinestation.entities.localmodels.QueryTopStationBody
import com.onlinestation.entities.responcemodels.stationmodels.ResponseStationList
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

interface RandomStationRepository {
    suspend fun getTopStations(queryBody: QueryTopStationBody): Result<List<StationItemResponse>>
}