package com.kmstore.onlinestation.fragment.topstations

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kmstore.onlinestation.appbase.viewmodel.BaseViewModel
import com.kmstore.onlinestation.data.entities.Constants.Companion.errorNotBalanceCode
import com.kmstore.onlinestation.data.entities.Constants.Companion.notFountIndexExaction
import com.kmstore.onlinestation.domain.interactors.AddStationDBInteractorUseCase
import com.kmstore.onlinestation.domain.interactors.TopStationInteractorUseCase
import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.util.NO_INTERNET_CONNECTION
import com.kmstore.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class TopStationViewModel(
    private val topStationInteractorUseCase: TopStationInteractorUseCase,
    private val addStationDBInteractorUseCase: AddStationDBInteractorUseCase
) : BaseViewModel() {

    private val _errorAddStation: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorAddStation = _errorAddStation.asStateFlow()

    private val _errorNotBalance: MutableSharedFlow<Unit> by lazy { MutableSharedFlow() }
    val errorNotBalance = _errorNotBalance.asSharedFlow()

    private val _getTopStation: MutableStateFlow<List<StationItem>?> by lazy { MutableStateFlow(null) }
    val getTopStation = _getTopStation.asStateFlow()

    private val _isLastPage by lazy { MutableSharedFlow<Boolean>() }
    val isLastPage = _isLastPage.asSharedFlow()

    private val _errorStationsList: MutableSharedFlow<Unit> by lazy { MutableSharedFlow() }
    val errorStationsList = _errorStationsList.asSharedFlow()

    init {
        getTopStationList()
    }

    fun getTopStationList(update: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = topStationInteractorUseCase(update,_getTopStation.value)) {

                is DataResult.Success -> withContext(Dispatchers.Main) {
                    result.data?.let {
                        _getTopStation.value = it.first
                        _isLastPage.emit(it.second)
                    }
                }

                is DataResult.Error -> withContext(Dispatchers.Main) {
                    if (result.errors.errorCode == NO_INTERNET_CONNECTION) {
                        showNotNetworksConnection()
                    }
                    _errorStationsList.emit(Unit)
                }
            }
            isShowEmptyData(_getTopStation.value.isNullOrEmpty())
            swipeRefreshState(false)
        }
    }

    fun addStationLocalDB(item: StationItem) {
        viewModelScope.launch {
            when (val result = addStationDBInteractorUseCase(item, _getTopStation.value)) {
                is DataResult.Success -> {
                    _getTopStation.value = result.data?.first
                    addedOrRemovedIsFavorite(result.data?.second)
                }
                is DataResult.Error -> {
                    when (result.errors.errorCode) {
                        errorNotBalanceCode -> {
                            _errorNotBalance.emit(Unit)
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