package com.kmstore.onlinestation.domain.usecases

import com.kmstore.onlinestation.data.repository.LocalSQLRepository
import com.kmstore.onlinestation.data.repository.GenreRepository
import com.kmstore.onlinestation.data.entities.Constants.Companion.defaultUserBalanceCount
import com.kmstore.onlinestation.data.entities.Constants.Companion.defaultUserID
import com.kmstore.onlinestation.domain.interactors.MainActivityInteractorUseCase
import com.kmstore.onlinestation.data.entities.OwnerUserBalance
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