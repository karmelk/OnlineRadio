package com.nextidea.onlinestation.data.dataservice.appservice

import com.nextidea.onlinestation.data.entities.OwnerUserBalance


interface PreferenceService {
    fun setUserDataPref(userData: OwnerUserBalance)
    fun getUserDataPref(): OwnerUserBalance

}