package com.onlinestation.data.repository

import com.onlinestation.data.dataservice.sqlservice.FavoriteDao
import com.onlinestation.data.datastore.FavoriteStationsRepository
import com.onlinestation.data.entities.stationmodels.StationItemDb

internal class FavoriteStationsRepositoryImpl(private val stationDao: FavoriteDao) :
    FavoriteStationsRepository {


    override suspend fun removeStationLocalDB(itemId: Int) {
        stationDao.deleteStationById(itemId)
    }

    override suspend fun addStationLocalDB(item: StationItemDb) {
        stationDao.insertStation(item)
    }

    override suspend fun getAllStationListLocalDB(): List<StationItemDb> =
        stationDao.getAllStationList()
}