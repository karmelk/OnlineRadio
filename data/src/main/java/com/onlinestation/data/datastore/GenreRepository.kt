package com.onlinestation.data.datastore

import com.onlinestation.data.entities.gendermodels.ResponseGender
import com.onlinestation.data.entities.request.QueryGenreBody
import com.onlinestation.data.entities.stationmodels.StationItemDb
import com.onlinestation.data.entities.Result

interface GenreRepository {
    suspend fun getGenreListData(queryBody: QueryGenreBody): Result<List<ResponseGender>>
    suspend fun checkStationInDB(itemId: Int) : StationItemDb?

}