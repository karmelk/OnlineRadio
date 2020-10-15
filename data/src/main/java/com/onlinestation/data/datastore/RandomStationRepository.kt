package com.onlinestation.data.datastore

import  com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryRandomStationBody
import com.onlinestation.entities.responcemodels.stationmodels.StationItem

interface RandomStationRepository {
    suspend fun getRandomStationData( queryBody: QueryRandomStationBody): Result<MutableList<StationItem>>
}