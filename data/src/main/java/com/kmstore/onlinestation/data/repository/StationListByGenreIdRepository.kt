package com.kmstore.onlinestation.data.repository

import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.entities.request.QueryStationByGenderBody
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemResponse

interface StationListByGenreIdRepository {

    suspend fun getStationListData(
        queryBody: QueryStationByGenderBody
    ): DataResult<List<StationItemResponse>>

}