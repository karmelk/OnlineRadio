package com.nextidea.onlinestation.domain.usecases


import com.nextidea.onlinestation.data.BuildConfig.API_KEY
import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.repository.LocalSQLRepository
import com.nextidea.onlinestation.data.repository.SearchStationRepository
import com.nextidea.onlinestation.data.entities.Constants.Companion.METHOD_GET_RADIOS
import com.nextidea.onlinestation.data.entities.Constants.Companion.RADIOS_LIMIT
import com.nextidea.onlinestation.data.entities.Constants.Companion.RADIOS_OFFSET
import com.nextidea.onlinestation.data.entities.Constants.Companion.errorDataEmpty
import com.nextidea.onlinestation.data.entities.Constants.Companion.errorDefaultCode
import com.nextidea.onlinestation.data.entities.RadioException

import com.nextidea.onlinestation.domain.interactors.SearchStationInteractorUseCase
import com.nextidea.onlinestation.data.entities.request.QuerySearchBody
import com.nextidea.onlinestation.domain.entities.StationItem
import com.nextidea.onlinestation.domain.utils.toDomain
import org.koin.core.annotation.Factory

@Factory
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