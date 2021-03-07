package com.onlinestation.domain.usecases

import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.utils.Constants.Companion.API_KEY
import com.kmworks.appbase.utils.Constants.Companion.RADIOS_LIMIT
import com.kmworks.appbase.utils.Constants.Companion.RADIOS_OFFSET
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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StationListByGenreIdUseCase(
    private val stationListByGenreIdRepository: StationListByGenreIdRepository,
    private val localSQLRepository: LocalSQLRepository
) : StationListByGenreIdInteractor {


    val stationItems: MutableList<StationItem> = mutableListOf()

    @Suppress("EXPERIMENTAL_API_USAGE")
    override suspend fun getStationListByGenreIdData(id: Long): Result<MutableList<StationItem>> {

        val apiList = stationListByGenreIdRepository.getStationListData(
            QueryStationByGenderBody(
                Constants.METHOD_GET_RADIOS, API_KEY, RADIOS_OFFSET,
                RADIOS_LIMIT, id
            )
        )
        return when (apiList) {
            is Result.Success -> {

                apiList.data?.let { stationList ->
                    for (item in stationList) {
                        val dbItem = localSQLRepository.getItemStationDB(item.id)
                        dbItem?.apply {
                            stationItems.add(
                                this.fromDBStationToStation(
                                    true
                                )
                            )
                        } ?: run {
                            stationItems.add(item.fromDBStationToStation(false))
                        }
                    }
                }
                Result.Success(stationItems)
            }
            else -> {
                Result.Error(RadioException(Constants.errorDefaultCode))
            }
        }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun addRemoveStationDataLocalDB(
        item: StationItem
    ): Result<MutableList<StationItem>> {
        val balanceCount: OwnerUserBalance? = getBalanceData()
        if (item.isFavorite) {
            CoroutineScope(Dispatchers.IO).launch {
                localSQLRepository.removeStationDB(itemId = item.id)
            }
            stationItems.find { it.id == item.id }?.isFavorite = false
            return Result.Success(stationItems)
        } else {
            balanceCount?.run {
                if (balance > 0) {
                    balance--
                    item.isFavorite = true
                    val itemLocalDb = item.fromStationToStationDB()
                    stationItems.find { it.id == item.id }?.isFavorite = true
                    CoroutineScope(Dispatchers.IO).launch {
                        localSQLRepository.addStationDB(itemLocalDb)
                        localSQLRepository.updateBalanceDB(this@run).collect { value ->

                        }
                    }
                    return Result.Success(stationItems)
                } else {
                    return Result.Error(RadioException(Constants.errorNotBalanceCode))
                }

            } ?: kotlin.run {
                return Result.Error(
                    RadioException(
                        Constants.errorNotBalanceCode
                    )
                )
            }
        }
    }

    override suspend fun removeStationDataLocalDB(itemId: Long) {
        localSQLRepository.removeStationDB(itemId)
    }

    override fun getBalanceData(): OwnerUserBalance? =
        localSQLRepository.getBalanceDB()

}