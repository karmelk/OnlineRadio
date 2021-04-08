package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.OwnerServerApiService
import com.onlinestation.data.datastore.RandomStationRepository
import com.onlinestation.data.util.analyzeResponse
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryTopStationBody
import com.onlinestation.entities.responcemodels.ParentResponse


import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse
import retrofit2.Response

class RandomStationRepositoryImpl(private val allApiService: OwnerServerApiService) :
    RandomStationRepository {

    override suspend fun getTopStations(queryBody: QueryTopStationBody): Result<List<StationItemResponse>> =

        makeApiCall({
            getTopStationData(
                allApiService.getTopStation(
                    queryBody.method,
                    queryBody.apiKey,
                    queryBody.offset,
                    queryBody.limit,
                    queryBody.isFeature
                )
            )
        })

    private fun getTopStationData(response: Response<ParentResponse<StationItemResponse>>): Result<List<StationItemResponse>> =
        analyzeResponse(response)

}