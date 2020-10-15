package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.datastore.RandomStationRepository
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.data.util.analyzeResponseStation
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryRandomStationBody


import com.onlinestation.entities.responcemodels.stationmodels.StationItem
import com.onlinestation.entities.responcemodels.stationmodels.ResponseObjectStation
import retrofit2.Response

class RandomStationRepositoryImpl(private val allApiService: AllApiService) :
    RandomStationRepository {
    override suspend fun getRandomStationData(
        queryBody: QueryRandomStationBody
    ): Result<MutableList<StationItem>> =

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

    private fun getRandomStationData(response: Response<ResponseObjectStation<StationItem>>): Result<MutableList<StationItem>> =
        analyzeResponseStation(response)

}