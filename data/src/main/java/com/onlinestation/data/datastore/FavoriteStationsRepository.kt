package com.onlinestation.data.datastore

import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal

interface FavoriteStationsRepository {
    suspend fun removeStationLocalDB(itemId: Int)
    suspend fun getAllStationListLocalDB():MutableList<StationItemLocal>
}