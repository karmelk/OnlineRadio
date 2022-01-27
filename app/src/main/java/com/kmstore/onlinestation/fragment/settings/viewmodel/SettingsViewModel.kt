package com.kmstore.onlinestation.fragment.settings.viewmodel

import androidx.lifecycle.viewModelScope
import com.kmstore.onlinestation.appbase.viewmodel.BaseViewModel
import com.kmstore.onlinestation.domain.interactors.SettingsInteractorUseCase
import com.kmstore.onlinestation.data.entities.OwnerUserBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(private val settingsInteractorUseCase: SettingsInteractorUseCase) :
    BaseViewModel() {

    private val _balanceCountDataLD: MutableStateFlow<OwnerUserBalance?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val balanceCountDataLD = _balanceCountDataLD.asStateFlow()

    init {
        getBalanceCount()
    }

    private fun getBalanceCount() {
        _balanceCountDataLD.value = settingsInteractorUseCase.getBalanceData()
    }

    fun updateBalance(defaultId: Int, balanceCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsInteractorUseCase.rewardBalanceDB(defaultId, balanceCount).collect {
                _balanceCountDataLD.emit(it)
            }
        }
    }

}