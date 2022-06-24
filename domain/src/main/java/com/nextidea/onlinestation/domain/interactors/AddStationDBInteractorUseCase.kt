package com.nextidea.onlinestation.domain.interactors

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.domain.entities.StationItem

interface AddStationDBInteractorUseCase {
    suspend operator fun invoke(
        item: StationItem,
        stationList: List<StationItem>?
    ): DataResult<Pair<List<StationItem>, StationItem>>
}