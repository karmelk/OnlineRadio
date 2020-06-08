package com.onlinestation.domain.interactors

import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import kotlinx.coroutines.flow.Flow

interface StationListByGenreIdInteractor {
    suspend fun getStationListByGenreIdData(parentId: Int): Result<MutableList<StationItemLocal>>
    suspend fun addStationDataLocalDB(item: StationItemLocal): Flow<Result<StationItemLocal>>
    suspend fun removeStationDataLocalDB(itemId: Int)
    fun getBalanceData(): OwnerUserBalance?
    //suspend fun setBalanceData(data: OwnerUserBalance):Flow<OwnerUserBalance>
}