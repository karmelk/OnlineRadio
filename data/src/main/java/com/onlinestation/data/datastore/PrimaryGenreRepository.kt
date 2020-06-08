package com.onlinestation.data.datastore

import com.onlinestation.entities.responcemodels.gendermodels.PrimaryGenreItem
import  com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryPrimaryGenreBody

interface PrimaryGenreRepository {
    suspend fun getPrimaryGenreData(queryBody: QueryPrimaryGenreBody): Result<MutableList<PrimaryGenreItem>>
    suspend fun saveGenreDB(genreData: MutableList<PrimaryGenreItem>)
    fun getPrimaryGenreDataDB(): MutableList<PrimaryGenreItem>?

}