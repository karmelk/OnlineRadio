package com.kmstore.onlinestation.data.repository

import com.kmstore.onlinestation.data.dataservice.sqlservice.FavoriteDao
import com.kmstore.onlinestation.data.datastore.FavoriteStationsRepository
import com.kmstore.onlinestation.data.entities.stationmodels.StationItemDb

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