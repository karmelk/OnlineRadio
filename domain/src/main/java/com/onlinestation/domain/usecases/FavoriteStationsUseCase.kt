package com.onlinestation.domain.usecases


import com.onlinestation.data.datastore.FavoriteStationsRepository
import com.onlinestation.domain.interactors.FavoriteStationsInteractor
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal

class FavoriteStationsUseCase(private val favoriteStationsRepository: FavoriteStationsRepository) :
    FavoriteStationsInteractor {

    override suspend fun removeStationDataLocalDB(itemId: Int) {
        favoriteStationsRepository.removeStationLocalDB(itemId)
    }

    override suspend fun getAllStationDataLocalDB(): MutableList<StationItemLocal> =
        favoriteStationsRepository.getAllStationListLocalDB()

}