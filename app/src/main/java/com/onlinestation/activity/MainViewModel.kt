package com.onlinestation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.onlinestation.appbase.viewmodel.BaseViewModel
import com.onlinestation.domain.interactors.MainActivityInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val mainActivityInteractor: MainActivityInteractor
) : BaseViewModel() {

    private val _addFavorite: MutableStateFlow<Boolean> by lazy { MutableStateFlow(false) }
    val isFavorite = _addFavorite.asStateFlow()

    private val _playPause: MutableStateFlow<Boolean?> by lazy { MutableStateFlow(false) }
    val playPause = _playPause.asStateFlow()

    private val _nextStation: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val nextStation = _nextStation.asStateFlow()

    private val _previewStation: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val previewStation = _previewStation.asStateFlow()

    private val _errorNotBalanceLD: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorNotBalanceLD = _errorNotBalanceLD.asStateFlow()

    val playPauseIcon = MutableStateFlow(false)

    fun initBalance() {
        mainActivityInteractor.getBalanceData()
    }

    fun checkStationInDB(stationID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _addFavorite.value = mainActivityInteractor.checkStationInDB(stationID)
            }
        }
    }


    fun playPause() {
        playPause.value?.let {
            _playPause.value = !it
        }
    }

    fun skipPreviousStation() {
        _previewStation.value = Unit
    }

    fun skipNextStation() {
        _nextStation.value = Unit
    }

}