package com.onlinestation.domain.interactors

interface MainActivityInteractor {
    suspend fun  checkStationInDB(itemId:Int): Boolean
    fun getBalanceData()

}