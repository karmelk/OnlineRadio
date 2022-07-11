package com.nextidea.onlinestation.data.repository

import com.nextidea.onlinestation.data.dataservice.apiservice.AllApiService
import com.nextidea.onlinestation.data.util.analyzeResponse
import com.nextidea.onlinestation.data.util.makeApiCall
import retrofit2.Response
import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.ParentResponse
import com.nextidea.onlinestation.data.entities.request.QuerySearchBody
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemResponse
import org.koin.core.annotation.Single

@Single
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
