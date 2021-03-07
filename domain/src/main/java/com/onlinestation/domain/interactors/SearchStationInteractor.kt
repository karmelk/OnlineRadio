package com.onlinestation.domain.interactors

import com.onlinestation.entities.responcemodels.OwnerUserBalance
import kotlinx.coroutines.flow.Flow
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse

interface SearchStationInteractor {
    suspend fun addStationDataLocalDB(item: StationItemResponse) : Flow<Result<StationItemResponse>>
    suspend fun searchStationListData(searchKeyword: String, genre:String?) : Result<MutableList<StationItemResponse>>
    suspend fun removeStationDataLocalDB(itemId: Int)
    fun getBalanceData(): OwnerUserBalance?
}