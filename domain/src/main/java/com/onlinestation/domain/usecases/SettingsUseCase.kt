package com.onlinestation.domain.usecases

import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.domain.interactors.SettingsInteractor
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import kotlinx.coroutines.flow.Flow

class SettingsUseCase(
    private val localSQLRepository: LocalSQLRepository
) : SettingsInteractor {
    override suspend fun rewardBalanceDB(defaultId: Int, balanceCount: Int): Flow<OwnerUserBalance> =
        localSQLRepository.rewardBalanceDB(OwnerUserBalance(defaultId, balanceCount))

    override fun getBalanceData(): OwnerUserBalance? =
        localSQLRepository.getBalanceDB()

}