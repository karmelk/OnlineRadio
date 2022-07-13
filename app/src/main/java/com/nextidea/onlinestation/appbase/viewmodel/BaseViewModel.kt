package com.nextidea.onlinestation.appbase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.nextidea.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _swipeRefreshState: MutableSharedFlow<Boolean> by lazy { MutableSharedFlow() }
    val swipeRefreshState = _swipeRefreshState.asSharedFlow()

    private val _isShowEmptyDataContainer: MutableStateFlow<Boolean?> by lazy { MutableStateFlow(null) }
    val isShowEmptyDataContainer = _isShowEmptyDataContainer.asStateFlow()

    private val _notNetworkConnection: MutableSharedFlow<Unit> by lazy { MutableSharedFlow() }
    val notNetworkConnection = _notNetworkConnection.asSharedFlow()

    private val _loadStation: MutableSharedFlow<String?> by lazy { MutableSharedFlow() }
    val loadStation = _loadStation.asSharedFlow()

    private val _addedOrRemovedFavorite: MutableStateFlow<StationItem?> by lazy {
        MutableStateFlow(
            StationItem.emptyItem()
        )
    }
    val addedOrRemovedFavorite = _addedOrRemovedFavorite.asStateFlow()

    fun loadData(mediaId: Long) {
        viewModelScope.launch {
            _loadStation.emit(mediaId.toString())
        }
    }

    fun isShowEmptyData(show: Boolean) {
        _isShowEmptyDataContainer.value = show
    }

    fun addedOrRemovedIsFavorite(item: StationItem?) {
        _addedOrRemovedFavorite.value = item
    }

    fun showNotNetworksConnection() {
        viewModelScope.launch {
            _notNetworkConnection.emit(Unit)
        }
    }

    fun swipeRefreshState(state:Boolean) {
        viewModelScope.launch {
            _swipeRefreshState.emit(state)
        }
    }

    fun getBasePagingConfig(
        pageSize: Int = 10,
        prefetchDistance: Int = 20,
        initialLoadSize: Int = 30
    ) = PagingConfig(
        pageSize = pageSize,
        prefetchDistance = prefetchDistance,
        initialLoadSize = initialLoadSize
    )
}