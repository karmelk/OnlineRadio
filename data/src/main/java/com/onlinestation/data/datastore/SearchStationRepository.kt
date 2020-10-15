package com.onlinestation.data.datastore

import  com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QuerySearchWithGenreBody
import com.onlinestation.entities.localmodels.QuerySearchWithoutGenreBody
import com.onlinestation.entities.responcemodels.stationmodels.StationItem

interface SearchStationRepository {
    suspend fun searchStationListData( queryBody: QuerySearchWithGenreBody): Result<MutableList<StationItem>>
    suspend fun searchStationListWithoutGenreData( queryBody: QuerySearchWithoutGenreBody): Result<MutableList<StationItem>>
}