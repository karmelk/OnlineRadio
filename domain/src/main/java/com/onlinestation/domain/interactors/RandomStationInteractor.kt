package com.onlinestation.domain.interactors

import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse
import kotlinx.coroutines.flow.Flow

interface RandomStationInteractor {
    suspend fun getRandomStationListData(): Result<MutableList<StationItemResponse>>
    suspend fun addStationDataLocalDB(
        item: StationItemResponse
    ): Flow<Result<StationItemResponse>>
    suspend fun removeStationDataLocalDB(itemId: Long)
    fun getBalanceData(): OwnerUserBalance?
    //suspend fun setBalanceData(data: OwnerUserBalance):Flow<OwnerUserBalance>
}