package com.kmstore.onlinestation.data.dataservice.appservice

import com.kmstore.onlinestation.data.entities.OwnerUserBalance


interface PreferenceService {
    fun setUserDataPref(userData: OwnerUserBalance)
    fun getUserDataPref(): OwnerUserBalance

}