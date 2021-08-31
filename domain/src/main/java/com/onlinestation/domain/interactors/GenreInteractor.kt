package com.onlinestation.domain.interactors

import com.onlinestation.data.entities.request.GenderItem
import com.onlinestation.data.entities.Result

interface GenreInteractor {
    suspend operator fun invoke(): Result<List<GenderItem>>
}