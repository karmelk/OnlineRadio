package com.kmstore.onlinestation.domain.usecases


import com.kmstore.onlinestation.data.BuildConfig.API_KEY
import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.repository.LocalSQLRepository
import com.kmstore.onlinestation.data.repository.SearchStationRepository
import com.kmstore.onlinestation.data.entities.Constants.Companion.METHOD_GET_RADIOS
import com.kmstore.onlinestation.data.entities.Constants.Companion.RADIOS_LIMIT
import com.kmstore.onlinestation.data.entities.Constants.Companion.RADIOS_OFFSET
import com.kmstore.onlinestation.data.entities.Constants.Companion.errorDataEmpty
import com.kmstore.onlinestation.data.entities.Constants.Companion.errorDefaultCode
import com.kmstore.onlinestation.data.entities.RadioException

import com.kmstore.onlinestation.domain.interactors.SearchStationInteractorUseCase
import com.kmstore.onlinestation.data.entities.request.QuerySearchBody
import com.kmstore.onlinestation.domain.entities.StationItem
import com.kmstore.onlinestation.domain.utils.toDomain

internal class SearchStationUseCaseImpl(
    private val localSQLRepository: LocalSQLRepository,
    private val searchRepository: SearchStationRepository
) : SearchStationInteractorUseCase {

    override suspend fun invoke(
        searchKeyword: String
    ): DataResult<List<StationItem>> {

        if(searchKeyword.isEmpty()){
            return DataResult.Error(RadioException(errorDataEmpty))
        }
        val apiList = searchRepository.searchStationListData(
            QuerySearchBody(
                METHOD_GET_RADIOS, API_KEY, RADIOS_OFFSET,
                RADIOS_LIMIT, searchKeyword
            )
        )
        return when (apiList) {
            is DataResult.Success -> {
                val updatedItems = (apiList.data?.map { it.toDomain(false) }
                    ?: listOf())
                    .map { localItem ->
                        val dbItem = localSQLRepository.getItemStationDB(localItem.id)
                        if (dbItem != null) {
                            localItem.copy(isFavorite = true)
                        } else {
                            localItem
                        }
                    }
                DataResult.Success(updatedItems)
            }
            else -> {
                DataResult.Error(RadioException(errorDefaultCode))
            }
        }
    }
}