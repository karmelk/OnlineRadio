package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.dataservice.apiservice.OwnerServerApiService
import com.onlinestation.data.datastore.StationListByGenreIdRepository
import com.onlinestation.data.util.analyzeResponse
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.data.util.analyzeResponseStation
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryStationByGenderBody
import com.onlinestation.entities.responcemodels.ParentResponse


import com.onlinestation.entities.responcemodels.stationmodels.ResponseObjectStation
import com.onlinestation.entities.responcemodels.stationmodels.ResponseStationList
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse
import retrofit2.Response

class StationListByGenreIdRepositoryImpl(private val allApiService: OwnerServerApiService) :
    StationListByGenreIdRepository {
    override suspend fun getStationListData(queryBody: QueryStationByGenderBody): Result<List<StationItemResponse>> =
        makeApiCall({
            getStationListGenreIdData(
                allApiService.getStationsByGenreId(
                    queryBody.method,
                    queryBody.apiKey,
                    queryBody.offset,
                    queryBody.limit,
                    queryBody.genre_id

                )
            )
        })

    private fun getStationListGenreIdData(response: Response<ParentResponse<StationItemResponse>>): Result<List<StationItemResponse>> =
        analyzeResponse(response)

}