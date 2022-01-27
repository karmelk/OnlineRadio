package com.kmstore.onlinestation.activity

import androidx.lifecycle.viewModelScope
import com.kmstore.onlinestation.appbase.viewmodel.BaseViewModel
import com.kmstore.onlinestation.domain.interactors.MainActivityInteractorUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val mainActivityInteractorUseCase: MainActivityInteractorUseCase
) : BaseViewModel() {

    private val _addFavorite: MutableStateFlow<Boolean> by lazy { MutableStateFlow(false) }
    val isFavorite get() = _addFavorite.asStateFlow()

    private val _playPause: MutableSharedFlow<Boolean> by lazy { MutableSharedFlow() }
    val playPause get() = _playPause.asSharedFlow()

    private val _nextStation: MutableSharedFlow<Unit> by lazy { MutableSharedFlow() }
    val nextStation get() = _nextStation.asSharedFlow()

    private val _previewStation: MutableSharedFlow<Unit> by lazy { MutableSharedFlow() }
    val previewStation get() = _previewStation.asSharedFlow()

    private val _errorNotBalanceLD: MutableSharedFlow<Unit> by lazy { MutableSharedFlow() }
    val errorNotBalanceLD get() = _errorNotBalanceLD.asSharedFlow()

    val playPauseIcon = MutableStateFlow(false)

    fun initBalance() {
        mainActivityInteractorUseCase.getBalanceData()
    }

    fun checkStationInDB(stationID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _addFavorite.value = mainActivityInteractorUseCase.checkStationInDB(stationID)
            }
        }
    }

    fun playPause(isPlaying: Boolean) {
        viewModelScope.launch {
            _playPause.emit(isPlaying)
        }
    }

    fun skipPreviousStation() {
        viewModelScope.launch {
            _previewStation.emit(Unit)
        }

    }

    fun skipNextStation() {
        viewModelScope.launch {
            _nextStation.emit(Unit)
        }
    }

}