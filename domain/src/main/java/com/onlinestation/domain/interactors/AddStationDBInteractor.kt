package com.onlinestation.domain.interactors

import com.onlinestation.domain.entities.StationItem
import com.onlinestation.data.entities.Result

interface AddStationDBInteractor {
    suspend operator fun invoke(
        item: StationItem,
        stationList: List<StationItem>?
    ): Result<Pair<List<StationItem>, StationItem>>
}