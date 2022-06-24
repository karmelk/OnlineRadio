package com.nextidea.onlinestation.data.repository

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.request.QueryStationByGenderBody
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemResponse

interface StationListByGenreIdRepository {

    suspend fun getStationListData(
        queryBody: QueryStationByGenderBody
    ): DataResult<List<StationItemResponse>>

}