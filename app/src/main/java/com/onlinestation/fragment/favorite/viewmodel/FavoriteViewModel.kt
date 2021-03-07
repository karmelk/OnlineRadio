package com.onlinestation.fragment.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.viewmodel.BaseViewModel
import com.onlinestation.domain.interactors.FavoriteStationsInteractor
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import com.onlinestation.service.PlayingRadioLibrary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(
    private val favoriteStationsInteractor: FavoriteStationsInteractor,
    private val playingRadioLibrary: PlayingRadioLibrary
) : BaseViewModel() {
    private val _getStationsListData by lazy { MutableLiveData<MutableList<StationItem>>() }
    val getStationsListData: LiveData<MutableList<StationItem>> get() = _getStationsListData

    init {
        getFavoriteStationList()
    }

    private fun getFavoriteStationList() {
        viewModelScope.launch(Dispatchers.Unconfined) {
            withContext(Dispatchers.Main) {
                _getStationsListData.value = favoriteStationsInteractor.getAllStationDataLocalDB()
            }
        }
    }

    fun removeFavoriteItem(itemId: Long) {

                _getStationsListData.value =
                    favoriteStationsInteractor.removeStationDataLocalDB(itemId)
        }



}