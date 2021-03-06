package com.onlinestation.fragment.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.BaseViewModel
import com.kmworks.appbase.Constants.Companion.defaultUserBalanceCount
import com.kmworks.appbase.Constants.Companion.defaultUserID
import com.onlinestation.domain.interactors.SettingsInteractor
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) :
    BaseViewModel() {
    val _balanceCountDataLD: MutableLiveData<OwnerUserBalance?> by lazy { MutableLiveData<OwnerUserBalance?>() }
    val balanceCountDataLD: LiveData<OwnerUserBalance?> get() = _balanceCountDataLD

    init {
        getBalanceCount()
    }

    private fun getBalanceCount() {
        _balanceCountDataLD.value = settingsInteractor.getBalanceData()
    }

    fun updateBalance(defaultId: Int=defaultUserID,balanceCount: Int=defaultUserBalanceCount) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsInteractor.rewardBalanceDB(defaultId,balanceCount).collect {
                withContext(Dispatchers.Main) {
                    _balanceCountDataLD.value = it
                }
            }
        }
    }

}