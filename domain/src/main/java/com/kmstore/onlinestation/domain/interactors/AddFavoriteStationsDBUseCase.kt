package com.kmstore.onlinestation.domain.interactors

import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.domain.entities.StationItem

interface AddFavoriteStationsDBUseCase {
    suspend fun addRemoveStationFromDB(item: StationItem): DataResult<Pair<List<StationItem>, StationItem>>
    suspend fun getAllStationDataLocalDB(): List<StationItem>
}