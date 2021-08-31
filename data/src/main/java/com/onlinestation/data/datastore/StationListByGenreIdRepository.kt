package com.onlinestation.data.datastore

import com.onlinestation.data.entities.Result
import com.onlinestation.data.entities.request.QueryStationByGenderBody
import com.onlinestation.data.entities.stationmodels.StationItemResponse

interface StationListByGenreIdRepository {

    suspend fun getStationListData(
        queryBody: QueryStationByGenderBody
    ): Result<List<StationItemResponse>>

}