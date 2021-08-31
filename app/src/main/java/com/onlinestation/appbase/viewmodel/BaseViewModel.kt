package com.onlinestation.appbase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel() {

    private val _showEmptyDataContainer: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val showEmptyDataContainer= _showEmptyDataContainer.asStateFlow()

    private val _notNetworkConnection: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val notNetworkConnection= _notNetworkConnection.asStateFlow()

    private val _loadStation: MutableStateFlow<String?> by lazy { MutableStateFlow(null) }
    val loadStation= _loadStation.asStateFlow()

    private val _addedOrRemovedIsFavorite: MutableStateFlow<StationItem?> by lazy { MutableStateFlow(StationItem.emptyItem()) }
    val addedOrRemovedIsFavorite= _addedOrRemovedIsFavorite.asStateFlow()

    fun loadData(mediaId: Long) {
        _loadStation.value = mediaId.toString()
    }

    fun showEmptyData() {
        _showEmptyDataContainer.value = Unit
    }

    fun addedOrRemovedIsFavorite(item: StationItem?) {
        _addedOrRemovedIsFavorite.value = item
    }

    fun showNotNetworksConnection() {
        _notNetworkConnection.value = Unit
    }
}