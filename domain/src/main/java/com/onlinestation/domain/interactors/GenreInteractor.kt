package com.onlinestation.domain.interactors

import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.GenderItem

interface GenreInteractor {
    suspend fun getGenreListData(): Result<List<GenderItem>>

}