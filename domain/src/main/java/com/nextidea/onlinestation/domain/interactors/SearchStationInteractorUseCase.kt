package com.nextidea.onlinestation.domain.interactors

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.domain.entities.StationItem

interface SearchStationInteractorUseCase {
    suspend operator fun invoke(searchKeyword: String) : DataResult<List<StationItem>>
}