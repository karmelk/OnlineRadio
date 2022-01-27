package com.kmstore.onlinestation.data.datastore

import com.kmstore.onlinestation.data.entities.stationmodels.StationItemDb

interface FavoriteStationsRepository {
    suspend fun removeStationLocalDB(itemId: Int)
    suspend fun addStationLocalDB(item: StationItemDb)
    suspend fun getAllStationListLocalDB():List<StationItemDb>
}