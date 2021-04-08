package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.OwnerServerApiService
import com.onlinestation.data.datastore.SearchStationRepository
import com.onlinestation.data.util.analyzeResponse
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.entities.localmodels.QuerySearchBody
import retrofit2.Response
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.ParentResponse
import com.onlinestation.entities.responcemodels.stationmodels.ResponseStationList
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

class SearchRepositoryImpl(private val allApiService: OwnerServerApiService) :
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
