package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.datastore.StationListByGenreIdRepository
import com.onlinestation.data.util.analyzeResponse
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.data.entities.ParentResponse
import com.onlinestation.data.entities.request.QueryStationByGenderBody
import com.onlinestation.data.entities.Result

import com.onlinestation.data.entities.stationmodels.StationItemResponse
import retrofit2.Response

internal class StationListByGenreIdRepositoryImpl(private val allApiService: AllApiService) :
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