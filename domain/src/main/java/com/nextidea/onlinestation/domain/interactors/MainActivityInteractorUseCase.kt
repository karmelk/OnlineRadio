package com.nextidea.onlinestation.domain.interactors

interface MainActivityInteractorUseCase {
    suspend fun  checkStationInDB(itemId:Int): Boolean
    fun getBalanceData()

}