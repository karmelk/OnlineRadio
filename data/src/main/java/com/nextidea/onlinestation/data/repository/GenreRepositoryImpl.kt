package com.nextidea.onlinestation.data.repository

import android.content.Context
import com.nextidea.onlinestation.data.dataservice.apiservice.AllApiService
import com.nextidea.onlinestation.data.util.analyzeResponse
import com.nextidea.onlinestation.data.util.makeApiCall
import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.ParentResponse
import com.nextidea.onlinestation.data.entities.RadioException
import com.nextidea.onlinestation.data.entities.gendermodels.ResponseGender
import com.nextidea.onlinestation.data.entities.request.QueryGenreBody
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemDb
import com.nextidea.onlinestation.data.util.NO_INTERNET_CONNECTION
import com.nextidea.onlinestation.data.util.hasNetwork
import org.koin.core.annotation.Single
import retrofit2.Response
@Single
internal class GenreRepositoryImpl(
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