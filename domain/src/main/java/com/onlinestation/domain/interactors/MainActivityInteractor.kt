package com.onlinestation.domain.interactors

import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.entities.Result

interface MainActivityInteractor {
    fun getGenderDB(): Result<MutableList<GenderItem>>
    suspend fun  checkStationInDB(itemId:Int): Boolean
    fun getBalanceData()
}