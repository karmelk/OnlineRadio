package com.onlinestation.domain.interactors

import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import kotlinx.coroutines.flow.Flow
import com.onlinestation.entities.Result

interface SearchStationInteractor {
    suspend fun addStationDataLocalDB(item: StationItemLocal) : Flow<Result<StationItemLocal>>
    suspend fun searchStationListData(searchKeyword: String, genre:String?) : Result<MutableList<StationItemLocal>>
    suspend fun removeStationDataLocalDB(itemId: Int)
    fun getBalanceData(): OwnerUserBalance?
}