package com.onlinestation.domain.interactors

import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.gendermodels.SecondaryGenreItem

interface SecondaryGenreInteractor {
    suspend fun getSecondaryGenreListData(parentId:Int): Result<MutableList<SecondaryGenreItem>>
}