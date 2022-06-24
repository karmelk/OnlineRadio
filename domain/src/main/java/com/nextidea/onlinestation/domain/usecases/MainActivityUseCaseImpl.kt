package com.nextidea.onlinestation.domain.usecases

import com.nextidea.onlinestation.data.repository.LocalSQLRepository
import com.nextidea.onlinestation.data.repository.GenreRepository
import com.nextidea.onlinestation.data.entities.Constants.Companion.defaultUserBalanceCount
import com.nextidea.onlinestation.data.entities.Constants.Companion.defaultUserID
import com.nextidea.onlinestation.domain.interactors.MainActivityInteractorUseCase
import com.nextidea.onlinestation.data.entities.OwnerUserBalance
import kotlinx.coroutines.*


internal class MainActivityUseCaseImpl(
    private val genreRepository: GenreRepository,
    private val localSQLRepository: LocalSQLRepository
) : MainActivityInteractorUseCase {

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