package com.onlinestation.data.dataservice.appservice

import com.onlinestation.data.entities.OwnerUserBalance


interface PreferenceService {
    fun setUserDataPref(userData: OwnerUserBalance)
    fun getUserDataPref(): OwnerUserBalance

}