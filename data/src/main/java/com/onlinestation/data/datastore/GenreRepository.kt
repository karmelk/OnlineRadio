package com.onlinestation.data.datastore

import com.onlinestation.entities.responcemodels.gendermodels.server.GenderItemDb
import  com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryGenreBody
import com.onlinestation.entities.responcemodels.gendermodels.server.ResponseGender
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemDb

interface GenreRepository {
    suspend fun getGenreListData(queryBody: QueryGenreBody): Result<List<ResponseGender>>
    suspend fun saveGenreDB(genderDatumDbs: List<GenderItemDb>)
    suspend fun getPrimaryGenreDataDB(): List<GenderItemDb>?
    suspend fun checkStationInDB(itemId: Int) : StationItemDb?

}