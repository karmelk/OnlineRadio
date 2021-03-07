package com.onlinestation.data.datastore

import com.onlinestation.entities.responcemodels.gendermodels.server.GenderItemDb
import  com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryGenreBody
import com.onlinestation.entities.responcemodels.gendermodels.server.ResponseGender
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemDb

interface GenreRepository {
    suspend fun getGenreListData(queryBody: QueryGenreBody): Result<List<ResponseGender>>
    suspend fun saveGenreDB(genderDatumDbs: List<GenderItemDb>)
    fun getPrimaryGenreDataDB(): MutableList<GenderItemDb>?
    suspend fun checkStationInDB(itemId: Long) : StationItemDb?

}