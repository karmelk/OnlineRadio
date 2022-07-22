package com.nextidea.onlinestation.data.repository

import android.content.Context
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.nextidea.onlinestation.data.dataservice.apiservice.AllApiService
import com.nextidea.onlinestation.data.dataservice.source.CountryPagingSource
import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.ParentResponse
import com.nextidea.onlinestation.data.entities.RadioException
import com.nextidea.onlinestation.data.entities.gendermodels.ResponseGender
import com.nextidea.onlinestation.data.entities.request.GenderItem
import com.nextidea.onlinestation.data.entities.request.QueryGenreBody
import com.nextidea.onlinestation.data.entities.stationmodels.StationItemDb
import com.nextidea.onlinestation.data.util.*
import org.koin.core.annotation.Single
import retrofit2.Response
import java.util.concurrent.Flow

@Single
internal class GenreRepositoryImpl(
    private val allApiService: AllApiService,
    private val localSQLRepository: LocalSQLRepository,
    private val context: Context
) : GenreRepository {

    override suspend fun getGenreListData(queryBody: QueryGenreBody): DataResult<List<ResponseGender>> {
        return if (context.hasNetwork()) {
            makeApiCall({
                newAnalyzeResponse(
                    allApiService.getGenreList(10,10)
                )
            })
        } else {
            DataResult.Error(RadioException(NO_INTERNET_CONNECTION))
        }
    }

    override fun getGenreListDataByPaging(): PagingSource<Int, GenderItem>  {
        return CountryPagingSource.Factory().create(allApiService)
    }

    override suspend fun checkStationInDB(itemId: Int): StationItemDb? =
        localSQLRepository.getItemStationDB(itemId)


}