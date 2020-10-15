package com.onlinestation.domain.interactors

import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.entities.Result

interface MainActivityInteractor {
    fun getPrimaryGenreDB(): Result<MutableList<GenderItem>>
}