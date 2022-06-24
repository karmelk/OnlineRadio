package com.nextidea.onlinestation.domain.interactors

import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.request.GenderItem

interface GenreInteractorUseCase {

    suspend operator fun invoke(
        update: Boolean,
        value: List<GenderItem>?
    ): DataResult<Pair<List<GenderItem>, Boolean>>
}