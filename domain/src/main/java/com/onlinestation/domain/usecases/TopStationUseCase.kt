package com.onlinestation.domain.usecases

import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.utils.Constants.Companion.API_KEY
import com.kmworks.appbase.utils.Constants.Companion.METHOD_GET_RADIOS
import com.kmworks.appbase.utils.Constants.Companion.RADIOS_LIMIT
import com.kmworks.appbase.utils.Constants.Companion.RADIOS_OFFSET
import com.kmworks.appbase.utils.Constants.Companion.notFountIndexExaction
import com.kmworks.appbase.utils.Constants.Companion.notFountItemIntoListExaction
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.RandomStationRepository
import com.onlinestation.domain.interactors.TopStationInteractor
import com.onlinestation.domain.utils.fromDBStationToStation
import com.onlinestation.domain.utils.fromStationToStationDB
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryTopStationBody
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopStationUseCase(
    private val randomStationRepository: RandomStationRepository,
    private val localSQLRepository: LocalSQLRepository
) : TopStationInteractor {
    private var stationItems: List<StationItem> = listOf()
    override suspend fun getTopStationList(): Result<List<StationItem>> {
        val apiData = randomStationRepository.getTopStations(
            QueryTopStationBody(
                METHOD_GET_RADIOS, API_KEY, RADIOS_OFFSET,
                RADIOS_LIMIT, 1
            )
        )

        return when (apiData) {
            is Result.Success -> {
                val updatedItems = (apiData.data?.map { it.fromDBStationToStation(false) }
                    ?: listOf())
                    .map { localItem ->
                        val dbItem = localSQLRepository.getItemStationDB(localItem.id)
                        if (dbItem != null) {
                            localItem.copy(isFavorite = true)
                        } else {
                            localItem
                        }
                    }
                    .also { stationItems = it }
                Result.Success(updatedItems)
            }
            else -> {
                Result.Error(RadioException(Constants.errorDefaultCode))
            }
        }
    }
//
//    override suspend fun addRemoveStationFromDB(
//        item: StationItem
//    ): Result<List<StationItem>> {
//        if (item.isFavorite) {
//            withContext(Dispatchers.IO) {
//                localSQLRepository.removeStationDB(item.id)
//            }
//            var updatedItem = stationItems.find { it.id == item.id } ?: return Result.Error(
//                RadioException(notFountItemIntoListExaction)
//            )
//            val index = stationItems.indexOf(updatedItem)
//            updatedItem = updatedItem.copy(isFavorite = false)
//            if (index == -1) {
//                return Result.Error(RadioException(notFountIndexExaction))
//            }
//            stationItems = stationItems.toMutableList().apply {
//                this[index] = updatedItem
//            }
//            return Result.Success(stationItems)
//        } else {
//            val balanceCount: OwnerUserBalance? = getBalanceData()
//            balanceCount?.run {
//                if (balance > 0) {
//                    val updateBalance=this.copy(balance = balance.minus(1))
//                    var updatedItem = stationItems.find { it.id == item.id } ?: return Result.Error(
//                        RadioException(notFountItemIntoListExaction)
//                    )
//
//                    val index = stationItems.indexOf(updatedItem)
//                    updatedItem = updatedItem.copy(isFavorite = true)
//                    if (index == -1) {
//                        return Result.Error(RadioException(notFountIndexExaction))
//                    }
//                    val itemLocalDb = updatedItem.fromStationToStationDB()
//                    withContext(Dispatchers.IO) {
//                        localSQLRepository.addStationDB(itemLocalDb)
//                        localSQLRepository.updateBalanceDB(updateBalance)
//                    }
//                    stationItems = stationItems.toMutableList().apply {
//                        this[index] = updatedItem
//                    }
//
//
//
//                    return Result.Success(stationItems)
//                } else {
//                    return Result.Error(RadioException(Constants.errorNotBalanceCode))
//                }
//
//            } ?: run {
//                return Result.Error(
//                    RadioException(
//                        Constants.errorNotBalanceCode
//                    )
//                )
//            }
//
//        }
//    }
//
//    override suspend fun removeStationDataLocalDB(itemId: Int) {
//        localSQLRepository.removeStationDB(itemId)
//    }
//
//    override fun getBalanceData(): OwnerUserBalance? =
//        localSQLRepository.getBalanceDB()

}