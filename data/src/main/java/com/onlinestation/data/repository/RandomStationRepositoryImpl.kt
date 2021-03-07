package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.datastore.RandomStationRepository
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.data.util.analyzeResponseStation
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryRandomStationBody


import com.onlinestation.entities.responcemodels.stationmodels.ResponseObjectStation
import com.onlinestation.entities.responcemodels.stationmodels.ResponseStationList
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse
import retrofit2.Response

class RandomStationRepositoryImpl(private val allApiService: AllApiService) :
    RandomStationRepository {
    override suspend fun getRandomStationData(
        queryBody: QueryRandomStationBody
    ): Result<ResponseStationList<StationItemResponse>> =

        makeApiCall({
            getRandomStationData(
                allApiService.getRandomStationList(
                    queryBody.apiKey,
                    queryBody.stationFormat,
                    queryBody.limit,
                    queryBody.dataFormat
                )
            )
        })

    private fun getRandomStationData(response: Response<ResponseObjectStation<StationItemResponse>>): Result<ResponseStationList<StationItemResponse>> =
        analyzeResponseStation(response)

}