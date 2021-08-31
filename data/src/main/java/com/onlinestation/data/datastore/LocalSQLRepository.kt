package com.onlinestation.data.datastore

import com.onlinestation.data.entities.OwnerUserBalance
import com.onlinestation.data.entities.gendermodels.GenderItemDb
import com.onlinestation.data.entities.stationmodels.StationItemDb
import kotlinx.coroutines.flow.Flow

interface LocalSQLRepository {
    suspend fun addStationDB(item: StationItemDb)
    suspend fun addGenreListDB(itemDb: List<GenderItemDb>)
    suspend fun getGenreListDB(): List<GenderItemDb>?
    suspend fun removeStationDB(itemId: Int)
    suspend fun getAllStationListDB(): List<StationItemDb>
    suspend fun getItemStationDB(id: Int): StationItemDb?
    fun getBalanceDB(): OwnerUserBalance?
    suspend fun updateBalanceDB(balanceCount: OwnerUserBalance)
    suspend fun rewardBalanceDB(balanceCount: OwnerUserBalance): Flow<OwnerUserBalance>
}