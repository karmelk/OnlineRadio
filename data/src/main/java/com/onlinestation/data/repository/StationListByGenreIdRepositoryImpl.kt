package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.datastore.StationListByGenreIdRepository
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.data.util.analyzeResponseStation
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryStationByGenderBody


import com.onlinestation.entities.responcemodels.stationmodels.StationItem
import com.onlinestation.entities.responcemodels.stationmodels.ResponseObjectStation
import retrofit2.Response

class StationListByGenreIdRepositoryImpl(private val allApiService: AllApiService) :
    StationListByGenreIdRepository {
    override suspend fun getStationListData(queryBody: QueryStationByGenderBody): Result<MutableList<StationItem>> =

        makeApiCall({
            getStationListGenreIdData(
                allApiService.getStationListByGenreId(
                    queryBody.genderId,
                    queryBody.limit,
                    queryBody.dataFormat,
                    queryBody.apiKey
                )
            )
        })

    private fun getStationListGenreIdData(response: Response<ResponseObjectStation<StationItem>>): Result<MutableList<StationItem>> =
        analyzeResponseStation(response)

}