package com.kmstore.onlinestation.domain.interactors

import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.domain.entities.StationItem

interface AddStationDBInteractorUseCase {
    suspend operator fun invoke(
        item: StationItem,
        stationList: List<StationItem>?
    ): DataResult<Pair<List<StationItem>, StationItem>>
}