package com.kmstore.onlinestation.domain.interactors

interface MainActivityInteractorUseCase {
    suspend fun  checkStationInDB(itemId:Int): Boolean
    fun getBalanceData()

}