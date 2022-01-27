package com.kmstore.onlinestation.data.repository

import com.kmstore.onlinestation.data.dataservice.apiservice.AllApiService
import com.kmstore.onlinestation.data.util.analyzeResponse
import com.kmstore.onlinestation.data.util.makeApiCall
import retrofit2.Response
import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.entities.ParentResponse
import com.kmstore.onlinestation.data.entities.request.QuerySearchBody
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemResponse

internal class SearchRepositoryImpl(private val allApiService: AllApiService) :
    SearchStationRepository {

    override suspend fun searchStationListData(queryBody: QuerySearchBody): DataResult<List<StationItemResponse>> =
        makeApiCall({
            getSearchStationData(
                allApiService.getSearchStationList(
                    queryBody.method,
                    queryBody.apiKey,
                    queryBody.offset,
                    queryBody.limit,
                    queryBody.keyword
                )
            )
        })



    private fun getSearchStationData(response: Response<ParentResponse<StationItemResponse>>): DataResult<List<StationItemResponse>> =
        analyzeResponse(response)

}
