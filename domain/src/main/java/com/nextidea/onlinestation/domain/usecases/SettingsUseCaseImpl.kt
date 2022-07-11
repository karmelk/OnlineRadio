package com.nextidea.onlinestation.domain.usecases

import com.nextidea.onlinestation.data.repository.LocalSQLRepository
import com.nextidea.onlinestation.domain.interactors.SettingsInteractorUseCase
import com.nextidea.onlinestation.data.entities.OwnerUserBalance
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Single

@Factory
internal class SettingsUseCaseImpl(
    private val localSQLRepository: LocalSQLRepository
) : SettingsInteractorUseCase {
    override suspend fun rewardBalanceDB(defaultId: Int, balanceCount: Int): Flow<OwnerUserBalance> =
        localSQLRepository.rewardBalanceDB(OwnerUserBalance(defaultId, balanceCount))

    override fun getBalanceData(): OwnerUserBalance? =
        localSQLRepository.getBalanceDB()

}