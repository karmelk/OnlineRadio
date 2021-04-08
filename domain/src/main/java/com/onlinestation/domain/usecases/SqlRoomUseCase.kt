package com.onlinestation.domain.usecases

import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.utils.Constants.Companion.notFountIndexExaction
import com.kmworks.appbase.utils.Constants.Companion.notFountItemIntoListExaction
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.domain.interactors.SqlRoomInteractor
import com.onlinestation.domain.utils.fromStationToStationDB
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SqlRoomUseCase(
    private val localSQLRepository: LocalSQLRepository
) : SqlRoomInteractor {
    private var stationItems: List<StationItem> = listOf()

    override suspend fun addRemoveStationFromDB(
        item: StationItem,
        stationList:List<StationItem>?
    ): Result<List<StationItem>> {
        if(stationList.isNullOrEmpty()){
            return Result.Error(
                RadioException(
                    Constants.errorDataNull
                )
            )
        }else {
            this.stationItems=stationList
            if (item.isFavorite) {
                withContext(Dispatchers.IO) {
                    localSQLRepository.removeStationDB(item.id)
                }
                var updatedItem = stationItems.find { it.id == item.id } ?: return Result.Error(
                    RadioException(notFountItemIntoListExaction)
                )
                val index = stationItems.indexOf(updatedItem)
                updatedItem = updatedItem.copy(isFavorite = false)
                if (index == -1) {
                    return Result.Error(RadioException(notFountIndexExaction))
                }
                stationItems = stationItems.toMutableList().apply {
                    this[index] = updatedItem
                }
                return Result.Success(stationItems)
            } else {
                val balanceCount: OwnerUserBalance? =localSQLRepository.getBalanceDB()
                balanceCount?.run {
                    if (balance > 0) {
                        val updateBalance = this.copy(balance = balance.minus(1))
                        var updatedItem =
                            stationItems.find { it.id == item.id } ?: return Result.Error(
                                RadioException(notFountItemIntoListExaction)
                            )

                        val index = stationItems.indexOf(updatedItem)
                        updatedItem = updatedItem.copy(isFavorite = true)
                        if (index == -1) {
                            return Result.Error(RadioException(notFountIndexExaction))
                        }
                        val itemLocalDb = updatedItem.fromStationToStationDB()
                        withContext(Dispatchers.IO) {
                            localSQLRepository.addStationDB(itemLocalDb)
                            localSQLRepository.updateBalanceDB(updateBalance)
                        }
                        stationItems = stationItems.toMutableList().apply {
                            this[index] = updatedItem
                        }



                        return Result.Success(stationItems)
                    } else {
                        return Result.Error(RadioException(Constants.errorNotBalanceCode))
                    }

                } ?: run {
                    return Result.Error(
                        RadioException(
                            Constants.errorNotBalanceCode
                        )
                    )
                }

            }
        }
    }


}