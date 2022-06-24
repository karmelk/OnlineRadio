package com.nextidea.onlinestation.domain.interactors

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.domain.entities.StationItem

interface TopStationInteractorUseCase {
    suspend operator fun invoke(update: Boolean, value: List<StationItem>?): DataResult<Pair<List<StationItem>, Boolean>>
}