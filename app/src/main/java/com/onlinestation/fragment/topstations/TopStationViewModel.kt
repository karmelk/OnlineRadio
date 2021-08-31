package com.onlinestation.fragment.topstations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.onlinestation.appbase.viewmodel.BaseViewModel
import com.onlinestation.data.entities.Constants.Companion.errorNotBalanceCode
import com.onlinestation.data.entities.Constants.Companion.notFountIndexExaction
import com.onlinestation.domain.interactors.AddStationDBInteractor
import com.onlinestation.domain.interactors.TopStationInteractor
import com.onlinestation.data.entities.Result
import com.onlinestation.data.util.NO_INTERNET_CONNECTION
import com.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

class TopStationViewModel(
    private val topStationInteractor: TopStationInteractor,
    private val addStationDBInteractor: AddStationDBInteractor
) : BaseViewModel() {

    private val _errorAddStation: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorAddStation = _errorAddStation.asStateFlow()

    private val _errorNotBalance: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorNotBalance = _errorNotBalance.asStateFlow()

    private val _getTopStation: MutableStateFlow<List<StationItem>?> by lazy { MutableStateFlow(null) }
    val getTopStation = _getTopStation.asStateFlow()

    private val _errorStationsList: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorStationsList = _errorStationsList.asStateFlow()

    fun getTopStationList() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userData = topStationInteractor()) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getTopStation.value = userData.data
                    if ( _getTopStation.value.isNullOrEmpty()) {
                        showEmptyData()
                    }
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    if (userData.errors.errorCode == NO_INTERNET_CONNECTION) {
                        showNotNetworksConnection()
                    }
                    _errorStationsList.value = Unit
                    if ( _getTopStation.value.isNullOrEmpty()) {
                        showEmptyData()
                    }
                }

            }
        }
    }

    fun addStationLocalDB(item: StationItem) {
        viewModelScope.launch {
            when (val result = addStationDBInteractor(item, _getTopStation.value)) {
                is Result.Success -> {
                    _getTopStation.value = result.data?.first
                    addedOrRemovedIsFavorite(result.data?.second)
                }
                is Result.Error -> {
                    when (result.errors.errorCode) {
                        errorNotBalanceCode -> {
                            _errorNotBalance.value = Unit
                        }
                        notFountIndexExaction -> {
                            _errorAddStation.value = Unit
                        }
                    }
                }
            }
        }
    }

}