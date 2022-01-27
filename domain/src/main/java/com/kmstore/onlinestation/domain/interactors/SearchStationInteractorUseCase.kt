package com.kmstore.onlinestation.domain.interactors

import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.domain.entities.StationItem

interface SearchStationInteractorUseCase {
    suspend operator fun invoke(searchKeyword: String) : DataResult<List<StationItem>>
}