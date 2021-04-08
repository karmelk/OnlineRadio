package com.onlinestation.domain.usecases


import com.kmworks.appbase.utils.Constants
import com.onlinestation.data.datastore.FavoriteStationsRepository
import com.onlinestation.domain.interactors.FavoriteStationsInteractor
import com.onlinestation.domain.utils.fromDBStationToStation
import com.onlinestation.entities.RadioException
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteStationsUseCase(private val favoriteStationsRepository: FavoriteStationsRepository) :
    FavoriteStationsInteractor {
    private var stationItems = listOf<StationItem>()
    override  suspend fun removeStationDataLocalDB(item: StationItem): Result<List<StationItem>> {

        val removeItem = stationItems.find { it.id == item.id } ?: return Result.Error(
            RadioException(Constants.notFountItemIntoListExaction)
        )
        withContext(Dispatchers.IO) {
            favoriteStationsRepository.removeStationLocalDB(item.id)
        }
        stationItems = stationItems.toMutableList().apply {
            this.remove(removeItem)
        }
        return Result.Success(stationItems)
    }

    override suspend fun getAllStationDataLocalDB(): List<StationItem> {
        stationItems = favoriteStationsRepository.getAllStationListLocalDB().map {
            it.fromDBStationToStation(true)
        }.toMutableList()

        return stationItems
    }

}