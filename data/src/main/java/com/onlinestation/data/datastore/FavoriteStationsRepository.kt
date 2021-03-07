package com.onlinestation.data.datastore

import com.onlinestation.entities.responcemodels.stationmodels.server.StationItemDb

interface FavoriteStationsRepository {
    suspend fun removeStationLocalDB(itemId: Long)
    suspend fun getAllStationListLocalDB():MutableList<StationItemDb>
}