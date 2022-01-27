package com.kmstore.onlinestation.data.repository

import com.kmstore.onlinestation.data.dataservice.sqlservice.BalanceDto
import com.kmstore.onlinestation.data.dataservice.sqlservice.FavoriteDao
import com.kmstore.onlinestation.data.dataservice.sqlservice.GenreDto
import com.kmstore.onlinestation.data.entities.OwnerUserBalance
import com.kmstore.onlinestation.data.entities.gendermodels.GenderItemDb
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemDb
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow

internal class LocalSQLRepositoryImpl(
    private val stationDao: FavoriteDao,
    private val balanceDto: BalanceDto,
    private val genreDto: GenreDto

) : LocalSQLRepository {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override suspend fun addStationDB(item: StationItemDb) = stationDao.insertStation(item)
    override suspend fun addGenreListDB(itemDb: List<GenderItemDb>) {
        genreDto.saveGenre(itemDb)
    }

    override suspend fun getGenreListDB(): List<GenderItemDb>? = genreDto.getGenreList()

    override suspend fun removeStationDB(itemId: Int) {
        stationDao.deleteStationById(itemId)
    }

    override suspend fun getAllStationListDB(): List<StationItemDb> =
        stationDao.getAllStationList()

    override suspend fun getItemStationDB(id: Int): StationItemDb? =
        stationDao.getItemStation(id)

    override fun getBalanceDB(): OwnerUserBalance? = balanceDto.getBalance()


    override suspend fun updateBalanceDB(balanceCount: OwnerUserBalance) {
        balanceDto.updateBalance(balanceCount)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    override suspend fun rewardBalanceDB(balanceCount: OwnerUserBalance) = flow {
            val currentBalance = getBalanceDB()
            currentBalance?.apply {
                val updateBalance = this.copy(balance = balance.plus(balanceCount.balance))
                balanceDto.updateBalance(updateBalance)
                emit(updateBalance)
            } ?: run {
                balanceDto.updateBalance(balanceCount)
                emit(balanceCount)
            }
        }

}