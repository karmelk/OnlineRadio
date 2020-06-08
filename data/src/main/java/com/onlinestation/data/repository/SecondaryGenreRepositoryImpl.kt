package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.datastore.SecondaryGenreRepository
import com.onlinestation.data.util.makeApiCall

import com.onlinestation.data.util.analyzeResponseGenre
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QuerySecondaryGenreBody
import com.onlinestation.entities.responcemodels.gendermodels.ResponseObjectGenre


import com.onlinestation.entities.responcemodels.gendermodels.SecondaryGenreItem
import retrofit2.Response

class SecondaryGenreRepositoryImpl(private val allApiService: AllApiService) :
    SecondaryGenreRepository {
    override suspend fun getSecondaryGenreData(queryBody: QuerySecondaryGenreBody): Result<MutableList<SecondaryGenreItem>> =
        makeApiCall({
            getPrimaryGenreData(
                allApiService.getSecondaryGenreList(
                    queryBody.genderId,
                    queryBody.apiKey,
                    queryBody.dataFormat
                )
            )
        })

    private fun getPrimaryGenreData(response: Response<ResponseObjectGenre<SecondaryGenreItem>>): Result<MutableList<SecondaryGenreItem>> =
        analyzeResponseGenre(response)

}