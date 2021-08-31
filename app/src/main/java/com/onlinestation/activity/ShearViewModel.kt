package com.onlinestation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ShearViewModel : ViewModel() {


    private val _favoriteStation by lazy { MutableSharedFlow<StationItem>() }
    val favoriteStation = _favoriteStation.asSharedFlow()

    fun sendFavoriteStation(station: StationItem?) {
        viewModelScope.launch {
            station?.let { it -> _favoriteStation.emit(it) }
        }
    }
}