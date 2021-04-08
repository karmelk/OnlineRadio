package com.onlinestation.fragment.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.viewmodel.BaseViewModel
import com.onlinestation.domain.interactors.FavoriteStationsInteractor
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import com.onlinestation.service.PlayingRadioLibrary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(
    private val favoriteStationsInteractor: FavoriteStationsInteractor,
    private val playingRadioLibrary: PlayingRadioLibrary
) : BaseViewModel() {
    private val _getStationsListData by lazy { MutableLiveData<List<StationItem>>() }
    val getStationsListData: LiveData<List<StationItem>> get() = _getStationsListData

    init {
        getFavoriteStationList()
    }

    private fun getFavoriteStationList() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _getStationsListData.value = favoriteStationsInteractor.getAllStationDataLocalDB()
            }
        }
    }

    fun removeFavoriteItem(item: StationItem) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val data = favoriteStationsInteractor.removeStationDataLocalDB(item)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getStationsListData.value = data.data
                }
                is Result.Error -> {
                    when (data.errors.errorCode) {

                    }
                }
            }
        }
    }


}