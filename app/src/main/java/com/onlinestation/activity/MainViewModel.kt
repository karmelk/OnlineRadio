package com.onlinestation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.viewmodel.BaseViewModel
import com.onlinestation.domain.interactors.MainActivityInteractor
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.GenderItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val mainActivityInteractor: MainActivityInteractor
) : BaseViewModel() {
    private val _primaryGenreDB by lazy { MutableLiveData<MutableList<GenderItem>>() }
    val primaryGenreDB: LiveData<MutableList<GenderItem>> get() = _primaryGenreDB

    private val _addFavorite = MutableLiveData<Boolean>(false)
    val isFavorite: LiveData<Boolean> get() = _addFavorite

    private val _playPause = MutableLiveData(false)
    val playPause: LiveData<Boolean> get() = _playPause
    val playPauseIcon = MutableLiveData(false)


    private val _nextStation = MutableLiveData(false)
    val nextStation: LiveData<Boolean> get() = _nextStation
    private val _previewStation = MutableLiveData(false)
    val previewStation: LiveData<Boolean> get() = _previewStation

    fun initBalance() {
        mainActivityInteractor.getBalanceData()
    }

    fun getGenderListDB() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userData = mainActivityInteractor.getGenderDB()) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _primaryGenreDB.value = userData.data
                }
            }
        }
    }

    fun checkStationInDB(stationID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _addFavorite.value = mainActivityInteractor.checkStationInDB(stationID)
            }
        }
    }

    fun addFavorite() {
        isFavorite.value?.let {
            _addFavorite.value = !it
        }
    }

    fun playPause() {
        playPause.value?.let {
            _playPause.value = !it
        }
    }

    fun skipPreviousStation() {
        _previewStation.value = true
    }

    fun skipNextStation() {
        _nextStation.value = true
    }

}