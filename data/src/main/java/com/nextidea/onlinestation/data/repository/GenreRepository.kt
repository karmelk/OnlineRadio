package com.nextidea.onlinestation.data.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.gendermodels.ResponseGender
import com.nextidea.onlinestation.data.entities.request.GenderItem
import com.nextidea.onlinestation.data.entities.request.QueryGenreBody
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemDb
import java.util.concurrent.Flow

interface GenreRepository {
    suspend fun getGenreListData(queryBody: QueryGenreBody): DataResult<List<ResponseGender>>
    suspend fun getGenreListDataByPaging(): Flow<PagingData<GenderItem>>
    suspend fun checkStationInDB(itemId: Int) : StationItemDb?

}