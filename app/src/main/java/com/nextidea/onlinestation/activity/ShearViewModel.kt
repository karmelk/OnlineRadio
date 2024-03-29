package com.nextidea.onlinestation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextidea.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ShearViewModel : ViewModel() {

    private val _favoriteStation by lazy { MutableSharedFlow<StationItem>() }
    val favoriteStation = _favoriteStation.asSharedFlow()

    fun sendFavoriteStation(station: StationItem?) {
        viewModelScope.launch {
            station?.let { it -> _favoriteStation.emit(it) }
        }
    }
}