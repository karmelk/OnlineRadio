package com.kmstore.onlinestation.domain.interactors

import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.domain.entities.StationItem

interface TopStationInteractorUseCase {
    suspend operator fun invoke(update: Boolean, value: List<StationItem>?): DataResult<Pair<List<StationItem>, Boolean>>
}