package com.kmstore.onlinestation.data.repository

import android.content.Context
import com.kmstore.onlinestation.data.dataservice.apiservice.AllApiService
import com.kmstore.onlinestation.data.util.analyzeResponse
import com.kmstore.onlinestation.data.util.makeApiCall
import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.entities.ParentResponse
import com.kmstore.onlinestation.data.entities.RadioException
import com.kmstore.onlinestation.data.entities.gendermodels.ResponseGender
import com.kmstore.onlinestation.data.entities.request.QueryGenreBody
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemDb
import com.kmstore.onlinestation.data.util.NO_INTERNET_CONNECTION
import com.kmstore.onlinestation.data.util.hasNetwork
import retrofit2.Response

class GenreRepositoryImpl(
    private val allApiService: AllApiService,
    private val localSQLRepository: LocalSQLRepository,
    private val context: Context
) : GenreRepository {

    override suspend fun getGenreListData(queryBody: QueryGenreBody): DataResult<List<ResponseGender>> {
        return if (context.hasNetwork()) {
            makeApiCall({
                getGenderData(
                    allApiService.getGenreList(queryBody.method, queryBody.apiKey)
                )
            })
        } else {
            DataResult.Error(RadioException(NO_INTERNET_CONNECTION))
        }
    }

    private fun getGenderData(response: Response<ParentResponse<ResponseGender>>): DataResult<List<ResponseGender>> =
        analyzeResponse(response)

    override suspend fun checkStationInDB(itemId: Int): StationItemDb? =
        localSQLRepository.getItemStationDB(itemId)


}