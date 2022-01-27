package com.kmstore.onlinestation.domain.interactors

import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.domain.entities.StationItem

interface StationListByGenreIdInteractorUseCase {
    suspend operator fun invoke(update: Boolean, id: Long): DataResult<Pair<List<StationItem>, Boolean>>
}