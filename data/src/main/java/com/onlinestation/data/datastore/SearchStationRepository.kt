package com.onlinestation.data.datastore

import  com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QuerySearchWithGenreBody
import com.onlinestation.entities.localmodels.QuerySearchWithoutGenreBody
import com.onlinestation.entities.responcemodels.stationmodels.ResponseStationList
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

interface SearchStationRepository {
    suspend fun searchStationListData( queryBody: QuerySearchWithGenreBody): Result<ResponseStationList<StationItemResponse>>
    suspend fun searchStationListWithoutGenreData( queryBody: QuerySearchWithoutGenreBody): Result<ResponseStationList<StationItemResponse>>
}