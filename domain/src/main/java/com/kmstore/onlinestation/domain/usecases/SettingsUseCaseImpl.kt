package com.kmstore.onlinestation.domain.usecases

import com.kmstore.onlinestation.data.repository.LocalSQLRepository
import com.kmstore.onlinestation.domain.interactors.SettingsInteractorUseCase
import com.kmstore.onlinestation.data.entities.OwnerUserBalance
import kotlinx.coroutines.flow.Flow

internal class SettingsUseCaseImpl(
    private val localSQLRepository: LocalSQLRepository
) : SettingsInteractorUseCase {
    override suspend fun rewardBalanceDB(defaultId: Int, balanceCount: Int): Flow<OwnerUserBalance> =
        localSQLRepository.rewardBalanceDB(OwnerUserBalance(defaultId, balanceCount))

    override fun getBalanceData(): OwnerUserBalance? =
        localSQLRepository.getBalanceDB()

}