package com.nextidea.onlinestation.domain.interactors

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.domain.entities.StationItem

interface AddFavoriteStationsDBUseCase {
    suspend fun addRemoveStationFromDB(item: StationItem): DataResult<Pair<List<StationItem>, StationItem>>
    suspend fun getAllStationDataLocalDB(): List<StationItem>
}