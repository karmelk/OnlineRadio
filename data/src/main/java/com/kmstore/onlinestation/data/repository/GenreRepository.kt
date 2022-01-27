package com.kmstore.onlinestation.data.repository

import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.entities.gendermodels.ResponseGender
import com.kmstore.onlinestation.data.entities.request.QueryGenreBody
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemDb

interface GenreRepository {
    suspend fun getGenreListData(queryBody: QueryGenreBody): DataResult<List<ResponseGender>>
    suspend fun checkStationInDB(itemId: Int) : StationItemDb?

}