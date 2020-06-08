package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.sqlservice.BalanceDto
import com.onlinestation.data.dataservice.sqlservice.FavoriteDao
import com.onlinestation.data.dataservice.sqlservice.GenreDto
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.gendermodels.PrimaryGenreItem
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow

class LocalSQLRepositoryImpl(
    private val stationDao: FavoriteDao,
    private val balanceDto: BalanceDto,
    private val genreDto: GenreDto

) : LocalSQLRepository {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override suspend fun addStationDB(item: StationItemLocal) = stationDao.insertStation(item)
    override suspend fun addGenreListDB(item: MutableList<PrimaryGenreItem>) {
        genreDto.saveGenre(item)
    }

    override fun getGenreListDB(): MutableList<PrimaryGenreItem>? =genreDto.getGenreList()

    override suspend fun removeStationDB(itemId: Int) {
        stationDao.deleteStationById(itemId)
    }

    override suspend fun getAllStationListDB(): MutableList<StationItemLocal> =
        stationDao.getAllStationList()

    override suspend fun getItemStationDB(itemId: Int): StationItemLocal? =
        stationDao.getItemStation(itemId)

    override fun getBalanceDB(): OwnerUserBalance? = balanceDto.getBalance()


    @Suppress("EXPERIMENTAL_API_USAGE", "NON_APPLICABLE_CALL_FOR_BUILDER_INFERENCE")
    override suspend fun updateBalanceDB(balanceCount: OwnerUserBalance) =
        channelFlow<OwnerUserBalance> {
            balanceDto.updateBalance(balanceCount)
            updateDefaultBalance(balanceCount, channel)
            awaitClose {}
        }

    @Suppress("EXPERIMENTAL_API_USAGE")
    override suspend fun rewardBalanceDB(balanceCount: OwnerUserBalance) =
        channelFlow<OwnerUserBalance> {
            val currentBalance = getBalanceDB()
            currentBalance?.apply {
                balance += balanceCount.balance
                balanceDto.updateBalance(this)
                updateDefaultBalance(this, channel)
            } ?: updateDefaultBalance(balanceCount, channel)

            awaitClose {}
        }

    private fun updateDefaultBalance(
        balanceCount: OwnerUserBalance,
        channel: SendChannel<OwnerUserBalance>
    ) {
        balanceDto.updateBalance(balanceCount)
        channel.offer(balanceCount)
    }
}