package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.PrimaryGenreRepository
import com.onlinestation.data.util.makeApiCall
import com.onlinestation.data.util.analyzeResponseGenre
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryPrimaryGenreBody
import com.onlinestation.entities.responcemodels.gendermodels.ResponseObjectGenre
import com.onlinestation.entities.responcemodels.gendermodels.PrimaryGenreItem
import retrofit2.Response
class PrimaryGenreRepositoryImpl(private val allApiService: AllApiService, private val localSQLRepository: LocalSQLRepository) :
    PrimaryGenreRepository {
    override suspend fun getPrimaryGenreData(queryBody: QueryPrimaryGenreBody): Result<MutableList<PrimaryGenreItem>> =
        makeApiCall({
            getPrimaryGenderData(
                allApiService.getPrimaryGenreList(
                    queryBody.apiKey,
                    queryBody.dataFormat
                )
            )
        })

    override suspend fun saveGenreDB(genreData: MutableList<PrimaryGenreItem>) {
        localSQLRepository.addGenreListDB(genreData)
    }

    override fun getPrimaryGenreDataDB(): MutableList<PrimaryGenreItem>? = localSQLRepository.getGenreListDB()

    private fun getPrimaryGenderData(response: Response<ResponseObjectGenre<PrimaryGenreItem>>): Result<MutableList<PrimaryGenreItem>> =
        analyzeResponseGenre(response)
}