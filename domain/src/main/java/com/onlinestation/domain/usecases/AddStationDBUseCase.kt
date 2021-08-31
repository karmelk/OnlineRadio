package com.onlinestation.domain.usecases

import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.entities.Constants.Companion.errorNotBalanceCode
import com.onlinestation.data.entities.Constants.Companion.notFountIndexExaction
import com.onlinestation.data.entities.Constants.Companion.notFountItemIntoListExaction
import com.onlinestation.domain.interactors.AddStationDBInteractor
import com.onlinestation.domain.utils.fromStationToStationDB
import com.onlinestation.data.entities.OwnerUserBalance
import com.onlinestation.data.entities.RadioException
import com.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.onlinestation.data.entities.Result

class AddStationDBUseCase(
    private val localSQLRepository: LocalSQLRepository
) : AddStationDBInteractor {

    private var stationItems: List<StationItem> = listOf()

    override suspend operator fun invoke(
        item: StationItem,
        stationList: List<StationItem>?
    ): Result<Pair<List<StationItem>, StationItem>> {
        if (stationList.isNullOrEmpty()) {
            withContext(Dispatchers.IO) {
                if (item.isFavorite) {
                    localSQLRepository.removeStationDB(item.id)
                } else {
                    localSQLRepository.getBalanceDB()?.run {
                        val updateBalance = this.copy(balance = balance.minus(1))
                        localSQLRepository.updateBalanceDB(updateBalance)
                        localSQLRepository.addStationDB(item.fromStationToStationDB())
                    }
                }
            }
            val updateStation = item.copy(isFavorite = !item.isFavorite)
            return Result.Success(Pair(stationItems, updateStation))
        } else {
            this.stationItems = stationList
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
                return Result.Success(Pair(stationItems, updatedItem))
            } else {
                val balanceCount: OwnerUserBalance? = localSQLRepository.getBalanceDB()
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
                        return Result.Success(Pair(stationItems, updatedItem))
                    } else {
                        return Result.Error(RadioException(errorNotBalanceCode))
                    }

                } ?: run {
                    return Result.Error(
                        RadioException(
                            errorNotBalanceCode
                        )
                    )
                }

            }
        }
    }
}
