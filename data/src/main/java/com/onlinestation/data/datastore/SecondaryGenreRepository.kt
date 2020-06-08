package com.onlinestation.data.datastore

import  com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QuerySecondaryGenreBody
import com.onlinestation.entities.responcemodels.gendermodels.SecondaryGenreItem

interface SecondaryGenreRepository {
    suspend fun getSecondaryGenreData(queryBody: QuerySecondaryGenreBody): Result<MutableList<SecondaryGenreItem>>
}