package com.onlinestation.domain.usecases

import com.kmworks.appbase.utils.Constants.Companion.API_KEY
import com.kmworks.appbase.utils.Constants.Companion.DATA_FORMAT
import com.kmworks.appbase.utils.Constants.Companion.LIMIT
import com.kmworks.appbase.utils.Constants.Companion.METHOD_GET_RADIOS
import com.kmworks.appbase.utils.Constants.Companion.errorDefaultCode
import com.onlinestation.data.datastore.LocalSQLRepository
import com.onlinestation.data.datastore.RandomStationRepository
import com.onlinestation.domain.interactors.RandomStationInteractor
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.QueryRandomStationBody
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow

class RandomStationUseCase(
    private val randomStationRepository: RandomStationRepository,
    private val localSQLRepository: LocalSQLRepository
) : RandomStationInteractor {

    override suspend fun getRandomStationListData(): Result<MutableList<StationItemResponse>> {
        val apiList = randomStationRepository.getRandomStationData(
            QueryRandomStationBody(
                API_KEY, METHOD_GET_RADIOS,
                LIMIT, DATA_FORMAT
            )
        )

        return when (apiList) {
            is Result.Success -> {
                val stationItemLocal: MutableList<StationItemResponse> = mutableListOf()
                apiList.data?.let {
                    it.station?.let {stationListData->
                        for (item in stationListData) {
                           /* val dbItem = localSQLRepository.getItemStationDB(item.id)
                            dbItem?.apply {
                                stationItemLocal.add(item.toLocalStation(createDateTime, isFavorite,it.tunein))
                            } ?: stationItemLocal.add(item.toLocalStation(0, false,it.tunein))*/
                        }
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
        item: StationItemResponse
    ) = channelFlow<Result<StationItemResponse>> {
        val balanceCount: OwnerUserBalance? = getBalanceData()
        /*balanceCount?.apply {
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
        */awaitClose {}
    }

    override suspend fun removeStationDataLocalDB(itemId: Long) {
        localSQLRepository.removeStationDB(itemId)
    }

    override fun getBalanceData(): OwnerUserBalance? =
        localSQLRepository.getBalanceDB()

}