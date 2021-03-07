package com.onlinestation.domain.usecases


import com.onlinestation.data.datastore.FavoriteStationsRepository
import com.onlinestation.domain.interactors.FavoriteStationsInteractor
import com.onlinestation.domain.utils.fromDBStationToStation
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteStationsUseCase(private val favoriteStationsRepository: FavoriteStationsRepository) :
    FavoriteStationsInteractor {
    private var stationList = mutableListOf<StationItem>()
    override  fun removeStationDataLocalDB(itemId: Long): MutableList<StationItem> {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteStationsRepository.removeStationLocalDB(itemId)
        }
        stationList.removeAll{it.id==itemId}
        return stationList
    }

    override suspend fun getAllStationDataLocalDB(): MutableList<StationItem> {
        stationList = favoriteStationsRepository.getAllStationListLocalDB().map {
            it.fromDBStationToStation(true)
        }.toMutableList()

        return stationList
    }

}