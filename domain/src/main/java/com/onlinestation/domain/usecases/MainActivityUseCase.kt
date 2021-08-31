package com.onlinestation.domain.usecases

import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.GenreRepository
import com.onlinestation.data.entities.Constants.Companion.defaultUserBalanceCount
import com.onlinestation.data.entities.Constants.Companion.defaultUserID
import com.onlinestation.domain.interactors.MainActivityInteractor
import com.onlinestation.data.entities.OwnerUserBalance
import kotlinx.coroutines.*


class MainActivityUseCase(
    private val genreRepository: GenreRepository,
    private val localSQLRepository: LocalSQLRepository
) : MainActivityInteractor {

    override suspend fun checkStationInDB(itemId: Int): Boolean =
        genreRepository.checkStationInDB(itemId)?.let {
            true
        } ?: false

    override fun getBalanceData() {
        val balanceData = localSQLRepository.getBalanceDB()
        if (balanceData == null) {
            CoroutineScope(Dispatchers.IO).launch {
                localSQLRepository.rewardBalanceDB(
                    OwnerUserBalance(
                        defaultUserID,
                        defaultUserBalanceCount
                    )
                )
            }
        }
    }
}