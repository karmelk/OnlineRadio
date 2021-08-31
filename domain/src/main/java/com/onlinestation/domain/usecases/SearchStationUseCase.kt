package com.onlinestation.domain.usecases


import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.SearchStationRepository
import com.onlinestation.data.entities.Constants.Companion.API_KEY
import com.onlinestation.data.entities.Constants.Companion.METHOD_GET_RADIOS
import com.onlinestation.data.entities.Constants.Companion.RADIOS_LIMIT
import com.onlinestation.data.entities.Constants.Companion.RADIOS_OFFSET
import com.onlinestation.data.entities.Constants.Companion.errorDataEmpty
import com.onlinestation.data.entities.Constants.Companion.errorDefaultCode
import com.onlinestation.data.entities.RadioException

import com.onlinestation.domain.interactors.SearchStationInteractor
import com.onlinestation.data.entities.request.QuerySearchBody
import com.onlinestation.domain.entities.StationItem
import com.onlinestation.data.entities.Result
import com.onlinestation.domain.utils.toDomain

class SearchStationUseCase(
    private val localSQLRepository: LocalSQLRepository,
    private val searchRepository: SearchStationRepository
) : SearchStationInteractor {

    override suspend fun invoke(
        searchKeyword: String
    ): Result<List<StationItem>> {

        if(searchKeyword.isEmpty()){
            return Result.Error(RadioException(errorDataEmpty))
        }
        val apiList = searchRepository.searchStationListData(
            QuerySearchBody(
                METHOD_GET_RADIOS, API_KEY, RADIOS_OFFSET,
                RADIOS_LIMIT, searchKeyword
            )
        )
        return when (apiList) {
            is Result.Success -> {
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
                Result.Success(updatedItems)
            }
            else -> {
                Result.Error(RadioException(errorDefaultCode))
            }
        }
    }
}