package com.onlinestation.domain.usecases

import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.RandomStationRepository
import com.kmworks.appbase.Constants.Companion.API_KEY
import com.kmworks.appbase.Constants.Companion.DATA_FORMAT
import com.kmworks.appbase.Constants.Companion.LIMIT
import com.kmworks.appbase.Constants.Companion.STATION_FORMAT
import com.kmworks.appbase.Constants.Companion.errorAddStationCode
import com.kmworks.appbase.Constants.Companion.errorDefaultCode
import com.kmworks.appbase.Constants.Companion.errorNotBalanceCode
import com.onlinestation.domain.interactors.RandomStationInteractor
import com.onlinestation.domain.utils.toLocalStation
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryRandomStationBody
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import java.util.*

class RandomStationUseCase(
    private val randomStationRepository: RandomStationRepository,
    private val localSQLRepository: LocalSQLRepository
) : RandomStationInteractor {

    override suspend fun getRandomStationListData(): Result<MutableList<StationItemLocal>> {
        val apiList = randomStationRepository.getRandomStationData(
            QueryRandomStationBody(
                API_KEY, STATION_FORMAT,
                LIMIT, DATA_FORMAT
            )
        )

        return when (apiList) {
            is Result.Success -> {
                val stationItemLocal: MutableList<StationItemLocal> = mutableListOf()
                apiList.data?.let {
                    for (item in it) {
                        val dbItem = localSQLRepository.getItemStationDB(item.id)
                        dbItem?.apply {
                            stationItemLocal.add(item.toLocalStation(createDateTime, isFavorite))
                        } ?: stationItemLocal.add(item.toLocalStation(0, false))
                    }
                }
                Result.Success(stationItemLocal)
            }
            else -> {
                Result.Error(RadioException(errorDefaultCode))
            }
        }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    override suspend fun addStationDataLocalDB(
        item: StationItemLocal
    ) = channelFlow<Result<StationItemLocal>> {
        val balanceCount: OwnerUserBalance? = getBalanceData()
        balanceCount?.apply {
            if (balance > 0) {
                balance--
                item.isFavorite = true
                item.createDateTime = Calendar.getInstance().time.time
                localSQLRepository.addStationDB(item)
                localSQLRepository.updateBalanceDB(this@apply).collect { value ->
                    channel.offer(Result.Success(item))
                }
            } else {
                channel.offer(Result.Error(RadioException(errorNotBalanceCode, item)))
            }
        } ?: channel.offer(Result.Error(RadioException(errorAddStationCode, item)))
        awaitClose {}
    }

    override suspend fun removeStationDataLocalDB(itemId: Int) {
        localSQLRepository.removeStationDB(itemId)
    }

    override fun getBalanceData(): OwnerUserBalance? =
        localSQLRepository.getBalanceDB()

}