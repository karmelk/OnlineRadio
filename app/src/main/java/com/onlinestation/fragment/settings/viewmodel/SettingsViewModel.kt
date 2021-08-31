package com.onlinestation.fragment.settings.viewmodel

import androidx.lifecycle.viewModelScope
import com.onlinestation.appbase.viewmodel.BaseViewModel
import com.onlinestation.domain.interactors.SettingsInteractor
import com.onlinestation.data.entities.OwnerUserBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : BaseViewModel() {

    private val _balanceCountDataLD: MutableStateFlow<OwnerUserBalance?> by lazy { MutableStateFlow(null) }
    val balanceCountDataLD = _balanceCountDataLD.asStateFlow()

    init {
        getBalanceCount()
    }

    private fun getBalanceCount() {
        _balanceCountDataLD.value = settingsInteractor.getBalanceData()
    }

    fun updateBalance(defaultId: Int,balanceCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsInteractor.rewardBalanceDB(defaultId,balanceCount).collect {
                withContext(Dispatchers.Main) {
                    _balanceCountDataLD.value = it
                }
            }
        }
    }

}