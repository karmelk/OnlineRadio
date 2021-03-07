package com.onlinestation.data.datastore

import  com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryStationByGenderBody
import com.onlinestation.entities.responcemodels.stationmodels.ResponseStationList
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

interface StationListByGenreIdRepository {
    suspend fun getStationListData(
        queryBody: QueryStationByGenderBody
    ): Result<List<StationItemResponse>>

}