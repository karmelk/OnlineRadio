package com.nextidea.onlinestation.domain.interactors

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.domain.entities.StationItem

interface StationListByGenreIdInteractorUseCase {
    suspend operator fun invoke(update: Boolean, id: Long): DataResult<Pair<List<StationItem>, Boolean>>
}