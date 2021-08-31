package com.onlinestation.domain.interactors

import com.onlinestation.domain.entities.StationItem
import com.onlinestation.data.entities.Result

interface SearchStationInteractor {
    suspend operator fun invoke(searchKeyword: String) : Result<List<StationItem>>
}