package com.onlinestation.domain.interactors

import com.onlinestation.domain.entities.StationItem
import com.onlinestation.data.entities.Result

interface AddFavoriteStationsDBInteractor {
    suspend fun addRemoveStationFromDB(item: StationItem):Result<Pair<List<StationItem>, StationItem>>
    suspend fun getAllStationDataLocalDB():List<StationItem>
}