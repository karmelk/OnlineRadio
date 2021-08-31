package com.onlinestation.fragment.stationsbygenderId.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.onlinestation.appbase.viewmodel.BaseViewModel
import com.onlinestation.data.entities.Constants.Companion.errorNotBalanceCode
import com.onlinestation.data.entities.Constants.Companion.notFountIndexExaction
import com.onlinestation.domain.interactors.AddStationDBInteractor
import com.onlinestation.domain.interactors.StationListByGenreIdInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.launch
import com.onlinestation.data.entities.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StationListByGenreIdViewModel(
    private val stationListByGenreIdInteractor: StationListByGenreIdInteractor,
    private val addStationDBInteractor: AddStationDBInteractor
) : BaseViewModel() {

    private val _errorNotBalance: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorNotBalance = _errorNotBalance.asStateFlow()

    private val _getStationsList: MutableStateFlow<List<StationItem>?> by lazy { MutableStateFlow(null) }
    val getStationsList = _getStationsList.asStateFlow()

    private val _errorLoadStations: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorLoadStations = _errorLoadStations.asStateFlow()

    private val _errorAddStation: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorAddStation = _errorAddStation.asStateFlow()

    fun getStationsByGenreIdList(genreId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            when (val result =
                stationListByGenreIdInteractor(genreId)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getStationsList.value = result.data
                    if (result.data.isNullOrEmpty()) {
                        showEmptyData()
                    }
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    _errorLoadStations.value = Unit
                }
            }
        }
    }

    fun addRemoveStationItem(item: StationItem) {
        viewModelScope.launch {
            when (val result =
                addStationDBInteractor(item, _getStationsList.value)) {
                is Result.Success -> {
                    _getStationsList.value = result.data?.first
                    addedOrRemovedIsFavorite(result.data?.second)
                    if (result.data?.first.isNullOrEmpty()) {
                        showEmptyData()
                    }
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

