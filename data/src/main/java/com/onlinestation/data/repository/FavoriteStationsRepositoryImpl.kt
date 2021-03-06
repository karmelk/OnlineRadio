package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.sqlservice.FavoriteDao
import com.onlinestation.data.datastore.FavoriteStationsRepository
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal

class FavoriteStationsRepositoryImpl(private val stationDao: FavoriteDao) :
    FavoriteStationsRepository {


    override suspend fun removeStationLocalDB(itemId: Int) {
        stationDao.deleteStationById(itemId)
    }

    override suspend fun getAllStationListLocalDB(): MutableList<StationItemLocal> =
        stationDao.getAllStationList()
}