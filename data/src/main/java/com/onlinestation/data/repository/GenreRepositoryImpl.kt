package com.onlinestation.data.repository

import android.content.Context
import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.GenreRepository
import com.onlinestation.data.util.analyzeResponse
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.data.entities.Result
import com.onlinestation.data.entities.ParentResponse
import com.onlinestation.data.entities.RadioException
import com.onlinestation.data.entities.gendermodels.GenderItemDb
import com.onlinestation.data.entities.gendermodels.ResponseGender
import com.onlinestation.data.entities.request.QueryGenreBody
import com.onlinestation.data.entities.stationmodels.StationItemDb
import com.onlinestation.data.util.NO_INTERNET_CONNECTION
import com.onlinestation.data.util.hasNetwork
import retrofit2.Response

internal class GenreRepositoryImpl(
    private val allApiService: AllApiService,
    private val localSQLRepository: LocalSQLRepository,
    private val context: Context
) : GenreRepository {

    override suspend fun getGenreListData(queryBody: QueryGenreBody): Result<List<ResponseGender>> {
        return if (context.hasNetwork()) {
            makeApiCall({
                getGenderData(
                    allApiService.getGenreList(queryBody.method, queryBody.apiKey)
                )
            })
        } else {
            Result.Error(RadioException(NO_INTERNET_CONNECTION))
        }
    }

    private fun getGenderData(response: Response<ParentResponse<ResponseGender>>): Result<List<ResponseGender>> =
        analyzeResponse(response)

    override suspend fun checkStationInDB(itemId: Int): StationItemDb? =
        localSQLRepository.getItemStationDB(itemId)


}