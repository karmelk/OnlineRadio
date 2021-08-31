package com.onlinestation.domain.interactors

import com.onlinestation.domain.entities.StationItem
import com.onlinestation.data.entities.Result

interface TopStationInteractor {
    suspend operator fun invoke(): Result<List<StationItem>>
}