package com.kmstore.onlinestation.domain.interactors

import com.kmstore.onlinestation.data.entities.OwnerUserBalance
import kotlinx.coroutines.flow.Flow


interface SettingsInteractorUseCase {
    suspend fun rewardBalanceDB(defaultId: Int, balanceCount: Int) : Flow<OwnerUserBalance>
    fun getBalanceData(): OwnerUserBalance?
}