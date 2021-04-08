package com.onlinestation.data.datastore

import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.gendermodels.server.GenderItemDb
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemDb
import kotlinx.coroutines.flow.Flow

interface LocalSQLRepository {
    suspend fun addStationDB(item: StationItemDb)
    suspend fun addGenreListDB(itemDb: List<GenderItemDb>)
   suspend fun getGenreListDB(): MutableList<GenderItemDb>?
    suspend fun removeStationDB(itemId: Int)
    suspend fun getAllStationListDB(): List<StationItemDb>
    suspend fun getItemStationDB(id: Int): StationItemDb?
    fun getBalanceDB(): OwnerUserBalance?
    suspend fun updateBalanceDB(balanceCount: OwnerUserBalance)
    suspend fun rewardBalanceDB(balanceCount: OwnerUserBalance): Flow<OwnerUserBalance>
}