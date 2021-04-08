package com.onlinestation.domain.usecases

import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.utils.Constants.Companion.API_KEY
import com.kmworks.appbase.utils.Constants.Companion.LIMIT
import com.kmworks.appbase.utils.Constants.Companion.OFFSET
import com.kmworks.appbase.utils.Constants.Companion.errorDataEmpty
import com.kmworks.appbase.utils.Constants.Companion.errorDefaultCode
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.SearchStationRepository

import com.onlinestation.domain.interactors.SearchStationInteractor
import com.onlinestation.domain.utils.fromDBStationToStation
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QuerySearchBody
import com.onlinestation.entities.localmodels.QueryTopStationBody
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem

class SearchStationUseCase(
    private val localSQLRepository: LocalSQLRepository,
    private val searchRepository: SearchStationRepository
) : SearchStationInteractor {

    override suspend fun searchStationListData(
        searchKeyword: String
    ): Result<List<StationItem>> {
        if(searchKeyword.isEmpty()){
            return Result.Error(RadioException(errorDataEmpty, listOf()))
        }
        val apiList = searchRepository.searchStationListData(
            QuerySearchBody(
                Constants.METHOD_GET_RADIOS, API_KEY, Constants.RADIOS_OFFSET,
                Constants.RADIOS_LIMIT, searchKeyword
            )
        )
        return when (apiList) {
            is Result.Success -> {
                val updatedItems = (apiList.data?.map { it.fromDBStationToStation(false) }
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