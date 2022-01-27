package com.kmstore.onlinestation.fragment.stationsbygenderId.viewmodel

import androidx.lifecycle.viewModelScope
import com.kmstore.onlinestation.appbase.viewmodel.BaseViewModel
import com.kmstore.onlinestation.data.entities.Constants.Companion.errorNotBalanceCode
import com.kmstore.onlinestation.data.entities.Constants.Companion.notFountIndexExaction
import com.kmstore.onlinestation.domain.interactors.AddStationDBInteractorUseCase
import com.kmstore.onlinestation.domain.interactors.StationListByGenreIdInteractorUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.kmstore.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.launch
import com.kmstore.onlinestation.data.entities.DataResult
import com.kmstore.onlinestation.data.util.NO_INTERNET_CONNECTION
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class StationListByGenreIdViewModel(
    private val stationListByGenreIdInteractorUseCase: StationListByGenreIdInteractorUseCase,
    private val addStationDBInteractorUseCase: AddStationDBInteractorUseCase
) : BaseViewModel() {

    private val _errorNotBalance by lazy { MutableSharedFlow<Unit>() }
    val errorNotBalance = _errorNotBalance.asSharedFlow()

    private val _getStationsList: MutableStateFlow<List<StationItem>?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val getStationsList = _getStationsList.asStateFlow()

    private val _errorLoadStations by lazy { MutableSharedFlow<Unit>() }
    val errorLoadStations = _errorLoadStations.asSharedFlow()

    private val _errorAddStation by lazy { MutableSharedFlow<Unit>() }
    val errorAddStation = _errorAddStation.asSharedFlow()

    private val _isLastPage by lazy { MutableSharedFlow<Boolean>() }
    val isLastPage = _isLastPage.asSharedFlow()

    fun getStationsByGenreIdList(genreId: Long, update: Boolean = false) {
        viewModelScope.launch(Dispatchers.Default) {
            when (val result =
                stationListByGenreIdInteractorUseCase(update, genreId)) {
                is DataResult.Success -> withContext(Dispatchers.Main) {
                    result.data?.let {
                        _getStationsList.value = it.first
                        _isLastPage.emit(it.second)
                    }
                }
                is DataResult.Error -> withContext(Dispatchers.Main) {
                    _errorLoadStations.emit(Unit)
                    if (result.errors.errorCode == NO_INTERNET_CONNECTION) {
                        showNotNetworksConnection()
                    }
                }
            }
            isShowEmptyData(_getStationsList.value.isNullOrEmpty())
            swipeRefreshState(false)
        }

    }

    fun addRemoveStationItem(item: StationItem) {
        viewModelScope.launch {
            when (val result =
                addStationDBInteractorUseCase(item, _getStationsList.value)) {
                is DataResult.Success -> {
                    _getStationsList.value = result.data?.first!!
                    addedOrRemovedIsFavorite(result.data?.second)
                    if (result.data?.first.isNullOrEmpty())
                        isShowEmptyData(true)
                    else
                        isShowEmptyData(false)
                }
                is DataResult.Error -> {
                    when (result.errors.errorCode) {
                        errorNotBalanceCode -> {
                            _errorNotBalance.emit(Unit)
                        }
                        notFountIndexExaction -> {
                            _errorAddStation.emit(Unit)
                        }
                    }
                }
            }
        }
    }

}

