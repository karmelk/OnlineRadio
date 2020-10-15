package com.onlinestation.data.dataservices.appservice

import com.onlinestation.entities.responcemodels.OwnerUserBalance


interface PreferenceService {
    fun setUserDataPref(userData: OwnerUserBalance)
    fun getUserDataPref(): OwnerUserBalance

}