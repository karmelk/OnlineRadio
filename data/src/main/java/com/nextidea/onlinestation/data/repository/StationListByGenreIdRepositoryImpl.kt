package com.nextidea.onlinestation.data.repository

import com.nextidea.onlinestation.data.dataservice.apiservice.AllApiService
import com.nextidea.onlinestation.data.util.analyzeResponse
import com.nextidea.onlinestation.data.util.makeApiCall
import com.nextidea.onlinestation.data.entities.ParentResponse
import com.nextidea.onlinestation.data.entities.request.QueryStationByGenderBody
import com.nextidea.onlinestation.data.entities.DataResult

import com.nextidea.onlinestation.data.entities.stationmodels.StationItemResponse
import org.koin.core.annotation.Single
import retrofit2.Response
@Single
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