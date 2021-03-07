package com.onlinestation.domain.interactors

import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.OwnerUserBalance

import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem

interface StationListByGenreIdInteractor {
    suspend fun getStationListByGenreIdData(id: Long): Result<MutableList<StationItem>>
    fun addRemoveStationDataLocalDB(item: StationItem): Result<MutableList<StationItem>>
    suspend fun removeStationDataLocalDB(itemId: Long)
    fun getBalanceData(): OwnerUserBalance?

    //suspend fun setBalanceData(data: OwnerUserBalance):Flow<OwnerUserBalance>
}