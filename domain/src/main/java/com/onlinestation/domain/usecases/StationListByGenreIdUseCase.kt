package com.onlinestation.domain.usecases

import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.utils.Constants.Companion.API_KEY
import com.kmworks.appbase.utils.Constants.Companion.RADIOS_LIMIT
import com.kmworks.appbase.utils.Constants.Companion.RADIOS_OFFSET
import com.kmworks.appbase.utils.Constants.Companion.notFountIndexExaction
import com.kmworks.appbase.utils.Constants.Companion.notFountItemIntoListExaction
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.StationListByGenreIdRepository
import com.onlinestation.domain.interactors.StationListByGenreIdInteractor
import com.onlinestation.domain.utils.fromDBStationToStation
import com.onlinestation.domain.utils.fromStationToStationDB
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryStationByGenderBody
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StationListByGenreIdUseCase(
    private val stationListByGenreIdRepository: StationListByGenreIdRepository,
    private val localSQLRepository: LocalSQLRepository
) : StationListByGenreIdInteractor {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override suspend fun getStationListByGenreIdData(id: Long): Result<List<StationItem>> =
        when (val apiList = stationListByGenreIdRepository.getStationListData(
            QueryStationByGenderBody(
                Constants.METHOD_GET_RADIOS, API_KEY, RADIOS_OFFSET,
                RADIOS_LIMIT, id
            )
        )) {
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
                Result.Error(RadioException(Constants.errorDefaultCode))
            }
        }

}