package com.onlinestation.data.datastore

import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.gendermodels.PrimaryGenreItem
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import kotlinx.coroutines.flow.Flow

interface LocalSQLRepository {
    suspend fun addStationDB(item: StationItemLocal)
    suspend fun addGenreListDB(item: MutableList<PrimaryGenreItem>)
    fun getGenreListDB(): MutableList<PrimaryGenreItem>?
    suspend fun removeStationDB(itemId: Int)
    suspend fun getAllStationListDB(): MutableList<StationItemLocal>
    suspend fun getItemStationDB(itemId: Int): StationItemLocal?
    fun getBalanceDB(): OwnerUserBalance?
    suspend fun updateBalanceDB(balanceCount: OwnerUserBalance): Flow<OwnerUserBalance>
    suspend fun rewardBalanceDB(balanceCount: OwnerUserBalance): Flow<OwnerUserBalance>
}