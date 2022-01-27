package com.kmstore.onlinestation.data.repository

import com.kmstore.onlinestation.data.dataservice.apiservice.AllApiService
import com.kmstore.onlinestation.data.util.analyzeResponse
import com.kmstore.onlinestation.data.util.makeApiCall
import com.kmstore.onlinestation.data.entities.ParentResponse
import com.kmstore.onlinestation.data.entities.request.QueryStationByGenderBody
import com.kmstore.onlinestation.data.entities.DataResult

import com.kmstore.onlinestation.data.entities.stationmodels.StationItemResponse
import retrofit2.Response

internal class StationListByGenreIdRepositoryImpl(private val allApiService: AllApiService) :
    StationListByGenreIdRepository {

    override suspend fun getStationListData(queryBody: QueryStationByGenderBody): DataResult<List<StationItemResponse>> =
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

    private fun getStationListGenreIdData(response: Response<ParentResponse<StationItemResponse>>): DataResult<List<StationItemResponse>> =
        analyzeResponse(response)
}