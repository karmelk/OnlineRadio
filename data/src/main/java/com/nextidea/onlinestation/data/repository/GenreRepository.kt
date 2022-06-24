package com.nextidea.onlinestation.data.repository

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.gendermodels.ResponseGender
import com.nextidea.onlinestation.data.entities.request.QueryGenreBody
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemDb

interface GenreRepository {
    suspend fun getGenreListData(queryBody: QueryGenreBody): DataResult<List<ResponseGender>>
    suspend fun checkStationInDB(itemId: Int) : StationItemDb?

}