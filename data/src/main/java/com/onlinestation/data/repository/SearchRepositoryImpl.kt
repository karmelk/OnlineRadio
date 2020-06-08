package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.datastore.SearchStationRepository
import com.onlinestation.data.util.analyzeResponseStation
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.entities.localmodels.QuerySearchWithGenreBody
import com.onlinestation.entities.responcemodels.stationmodels.ResponseObjectStation
import com.onlinestation.entities.responcemodels.stationmodels.StationItem
import retrofit2.Response
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QuerySearchWithoutGenreBody

class SearchRepositoryImpl(private val allApiService: AllApiService) :
    SearchStationRepository {

    override suspend fun searchStationListData(queryBody: QuerySearchWithGenreBody): Result<MutableList<StationItem>> =
        makeApiCall({
            getSearchStationData(
                allApiService.getSearchStationList(
                    queryBody.ct,
                    queryBody.dataFormat,
                    queryBody.limit,
                    queryBody.genre,
                    queryBody.apiKey
                )
            )
        })

    override suspend fun searchStationListWithoutGenreData(queryBody: QuerySearchWithoutGenreBody): Result<MutableList<StationItem>> =
        makeApiCall({
            getSearchStationData(
                allApiService.getSearchStationWithoutStationList(
                    queryBody.ct,
                    queryBody.dataFormat,
                    queryBody.limit,
                    queryBody.apiKey
                )
            )
        })

    private fun getSearchStationData(response: Response<ResponseObjectStation<StationItem>>): Result<MutableList<StationItem>> =
        analyzeResponseStation(response)

}
