package com.nextidea.onlinestation.data.dataservice.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nextidea.onlinestation.data.dataservice.apiservice.AllApiService
import com.nextidea.onlinestation.data.entities.gendermodels.ResponseGender
import com.nextidea.onlinestation.data.entities.gendermodels.ResponseGender.Companion.toDomain
import com.nextidea.onlinestation.data.entities.request.GenderItem
import retrofit2.HttpException
import java.io.IOException

class CountryPagingSource (
    private val service: AllApiService
) : PagingSource<Int, GenderItem>() {

     val STARTING_PAGE_INDEX = 1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GenderItem> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getGenreList(params.loadSize, page)
            val players = response.body()?.map { it.toDomain() }?: emptyList()
            LoadResult.Page(
                data = players,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey =  page + 1
            )

        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, GenderItem>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    class Factory{
        fun create(orderApi: AllApiService) = CountryPagingSource(orderApi)
    }
}