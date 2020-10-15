package com.onlinestation.domain.interactors

import com.onlinestation.entities.responcemodels.OwnerUserBalance
import kotlinx.coroutines.flow.Flow


interface SettingsInteractor {
    suspend fun rewardBalanceDB(defaultId: Int, balanceCount: Int) :Flow<OwnerUserBalance>
    fun getBalanceData(): OwnerUserBalance?
}