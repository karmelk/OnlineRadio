package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.datastore.SearchStationRepository
import com.onlinestation.data.util.analyzeResponse
import com.onlinestation.data.util.makeApiCall
import retrofit2.Response
import com.onlinestation.data.entities.Result
import com.onlinestation.data.entities.ParentResponse
import com.onlinestation.data.entities.request.QuerySearchBody
import com.onlinestation.data.entities.stationmodels.StationItemResponse

internal class SearchRepositoryImpl(private val allApiService: AllApiService) :
    SearchStationRepository {

    override suspend fun searchStationListData(queryBody: QuerySearchBody): Result<List<StationItemResponse>> =
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



    private fun getSearchStationData(response: Response<ParentResponse<StationItemResponse>>): Result<List<StationItemResponse>> =
        analyzeResponse(response)

}
