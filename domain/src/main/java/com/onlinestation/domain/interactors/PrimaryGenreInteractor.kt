package com.onlinestation.domain.interactors

import com.onlinestation.entities.responcemodels.gendermodels.PrimaryGenreItem
import com.onlinestation.entities.Result

interface PrimaryGenreInteractor {
    suspend fun getPrimaryGenreListData(): Result<MutableList<PrimaryGenreItem>>
    fun getPrimaryGenreListDataDB(): Result<MutableList<PrimaryGenreItem>>
}