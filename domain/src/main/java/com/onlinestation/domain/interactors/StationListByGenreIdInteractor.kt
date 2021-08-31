package com.onlinestation.domain.interactors

import com.onlinestation.domain.entities.StationItem
import com.onlinestation.data.entities.Result

interface StationListByGenreIdInteractor {
    suspend operator fun invoke(id: Long): Result<List<StationItem>>
}