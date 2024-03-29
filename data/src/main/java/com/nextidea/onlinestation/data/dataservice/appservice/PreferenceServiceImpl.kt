package com.nextidea.onlinestation.data.dataservice.appservice

import android.content.Context
import com.nextidea.onlinestation.data.entities.OwnerUserBalance
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.koin.core.annotation.Single

@Single
internal class PreferenceServiceImpl(private val context: Context) : PreferenceService {

    private val authenticationKey = "authenticationKey"

    override fun getUserDataPref(): OwnerUserBalance {
        val jsonAdapter: JsonAdapter<OwnerUserBalance> = Moshi.Builder().build().adapter(
            OwnerUserBalance::class.java)
        val json = context.getSharedPreferences("userData", Context.MODE_PRIVATE).getString(authenticationKey, "")
        return jsonAdapter.fromJson(json!!)!!
    }

    override fun setUserDataPref(userData: OwnerUserBalance) {
        val jsonAdapter: JsonAdapter<OwnerUserBalance> = Moshi.Builder().build().adapter(
            OwnerUserBalance::class.java)
        context.getSharedPreferences("userData", Context.MODE_PRIVATE).edit()
                .putString(authenticationKey, jsonAdapter.toJson(userData)).apply()
    }

   /* var accessToken: String
        get() = sharedPreferences.getString(KEY_ACCESS_TOKEN, "") ?: ""
        set(accessToken) = sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply()*/

}