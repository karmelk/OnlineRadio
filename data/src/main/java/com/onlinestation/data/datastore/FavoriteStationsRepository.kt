package com.onlinestation.data.datastore

import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemDb

interface FavoriteStationsRepository {
    suspend fun removeStationLocalDB(itemId: Int)
    suspend fun getAllStationListLocalDB():List<StationItemDb>
}