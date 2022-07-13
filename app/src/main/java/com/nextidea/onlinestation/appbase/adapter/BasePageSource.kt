package com.nextidea.onlinestation.appbase.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nextidea.onlinestation.data.entities.DataResult


open class BasePageSource<T : Any> : PagingSource<Int, T>() {

    private var pageNumber: Int = 0

    private var pageSize: Int = 0

    override fun getRefreshKey(state: PagingState<Int, T>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> =
        LoadResult.Error(Throwable("Default error base paging"))

    private fun nextKey(list: List<Any>) =
        if (list.size < pageSize) null else pageNumber + 1

    private fun prevKey() =
        if (pageNumber == 1) null else pageNumber - 1

    suspend operator fun LoadParams<Int>.invoke(
        call: suspend (pageNumber: Int, pageSize: Int) -> DataResult<List<T>>,
    ): LoadResult<Int, T> {
        pageNumber = this.key ?: 1
        pageSize = this.loadSize

        return when (val result = call(pageNumber, pageSize)) {
            is DataResult.Success -> {
                LoadResult.Page(result.data, prevKey(), nextKey(result.data))
            }

            is DataResult.Error -> {
                LoadResult.Error(result.errors)
            }
        }
    }

}
