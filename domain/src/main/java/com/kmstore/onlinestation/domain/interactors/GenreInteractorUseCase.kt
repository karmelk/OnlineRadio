package com.kmstore.onlinestation.domain.interactors

import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.entities.request.GenderItem

interface GenreInteractorUseCase {

    suspend operator fun invoke(
        update: Boolean,
        value: List<GenderItem>?
    ): DataResult<Pair<List<GenderItem>, Boolean>>
}