package com.nextidea.onlinestation.domain.usecases

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.repository.LocalSQLRepository
import com.nextidea.onlinestation.data.entities.Constants.Companion.errorNotBalanceCode
import com.nextidea.onlinestation.data.entities.Constants.Companion.notFountIndexExaction
import com.nextidea.onlinestation.data.entities.Constants.Companion.notFountItemIntoListExaction
import com.nextidea.onlinestation.domain.interactors.AddStationDBInteractorUseCase
import com.nextidea.onlinestation.domain.utils.fromStationToStationDB
import com.nextidea.onlinestation.data.entities.OwnerUserBalance
import com.nextidea.onlinestation.data.entities.RadioException
import com.nextidea.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
internal class AddStationDBUseCaseImpl(
    private val localSQLRepository: LocalSQLRepository
) : AddStationDBInteractorUseCase {

    private var stationItems: List<StationItem> = listOf()

    override suspend operator fun invoke(
        item: StationItem,
        stationList: List<StationItem>?
    ): DataResult<Pair<List<StationItem>, StationItem>> {
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
            return DataResult.Success(Pair(stationItems, updateStation))
        } else {
            this.stationItems = stationList
            if (item.isFavorite) {
                withContext(Dispatchers.IO) {
                    localSQLRepository.removeStationDB(item.id)
                }
                var updatedItem = stationItems.find { it.id == item.id } ?: return DataResult.Error(
                    RadioException(notFountItemIntoListExaction)
                )
                val index = stationItems.indexOf(updatedItem)
                updatedItem = updatedItem.copy(isFavorite = false)
                if (index == -1) {
                    return DataResult.Error(RadioException(notFountIndexExaction))
                }
                stationItems = stationItems.toMutableList().apply {
                    this[index] = updatedItem
                }
                return DataResult.Success(Pair(stationItems, updatedItem))
            } else {
                val balanceCount: OwnerUserBalance? = localSQLRepository.getBalanceDB()
                balanceCount?.run {
                    if (balance > 0) {
                        val updateBalance = this.copy(balance = balance.minus(1))
                        var updatedItem =
                            stationItems.find { it.id == item.id } ?: return DataResult.Error(
                                RadioException(notFountItemIntoListExaction)
                            )

                        val index = stationItems.indexOf(updatedItem)
                        updatedItem = updatedItem.copy(isFavorite = true)
                        if (index == -1) {
                            return DataResult.Error(RadioException(notFountIndexExaction))
                        }
                        val itemLocalDb = updatedItem.fromStationToStationDB()
                        withContext(Dispatchers.IO) {
                            localSQLRepository.addStationDB(itemLocalDb)
                            localSQLRepository.updateBalanceDB(updateBalance)
                        }
                        stationItems = stationItems.toMutableList().apply {
                            this[index] = updatedItem
                        }
                        return DataResult.Success(Pair(stationItems, updatedItem))
                    } else {
                        return DataResult.Error(RadioException(errorNotBalanceCode))
                    }

                } ?: run {
                    return DataResult.Error(
                        RadioException(
                            errorNotBalanceCode
                        )
                    )
                }

            }
        }
    }
}
