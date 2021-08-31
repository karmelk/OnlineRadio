package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.sqlservice.BalanceDto
import com.onlinestation.data.dataservice.sqlservice.FavoriteDao
import com.onlinestation.data.dataservice.sqlservice.GenreDto
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.entities.OwnerUserBalance
import com.onlinestation.data.entities.gendermodels.GenderItemDb
import com.onlinestation.data.entities.stationmodels.StationItemDb
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow

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
    override suspend fun rewardBalanceDB(balanceCount: OwnerUserBalance) =
        channelFlow<OwnerUserBalance> {
            val currentBalance = getBalanceDB()
            currentBalance?.apply {
                val updateBalance = this.copy(balance = balance.plus(balanceCount.balance))
                balanceDto.updateBalance(updateBalance)
                channel.send(updateBalance)
            } ?: run {
                balanceDto.updateBalance(balanceCount)
                channel.send(balanceCount)
            }
            awaitClose {}
        }

}