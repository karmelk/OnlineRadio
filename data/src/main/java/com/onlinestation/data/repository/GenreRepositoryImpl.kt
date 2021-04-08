package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.OwnerServerApiService
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.GenreRepository
import com.onlinestation.data.util.analyzeResponse
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.data.util.analyzeResponseGenre
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryGenreBody
import com.onlinestation.entities.responcemodels.ParentResponse
import com.onlinestation.entities.responcemodels.gendermodels.ResponseObjectGenre
import com.onlinestation.entities.responcemodels.gendermodels.server.GenderItemDb
import com.onlinestation.entities.responcemodels.gendermodels.server.ResponseGender
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemDb
import retrofit2.Response
class GenreRepositoryImpl(private val ownerServerApiService: OwnerServerApiService,
                          private val localSQLRepository: LocalSQLRepository) : GenreRepository {

    override suspend fun getGenreListData(queryBody: QueryGenreBody): Result<List<ResponseGender>> =
        makeApiCall({
            getGenderData(
                ownerServerApiService.getGenreList(queryBody.method,queryBody.apiKey)
            )
        })


    override suspend fun saveGenreDB(genderDatumDbs: List<GenderItemDb>) {
        localSQLRepository.addGenreListDB(genderDatumDbs)
    }

    override suspend fun getPrimaryGenreDataDB(): List<GenderItemDb>? = localSQLRepository.getGenreListDB()

    private fun getPrimaryGenderData(response: Response<ResponseObjectGenre<GenderItemDb>>): Result<MutableList<GenderItemDb>> =
        analyzeResponseGenre(response)

    private fun getGenderData(response: Response<ParentResponse<ResponseGender>>): Result<List<ResponseGender>> =
        analyzeResponse(response)

    override suspend fun checkStationInDB(itemId: Int): StationItemDb? =localSQLRepository.getItemStationDB(itemId)


}