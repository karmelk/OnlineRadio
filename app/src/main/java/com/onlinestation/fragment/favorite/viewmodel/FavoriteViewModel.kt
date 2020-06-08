package com.onlinestation.fragment.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.BaseViewModel
import com.onlinestation.domain.interactors.FavoriteStationsInteractor
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(private val favoriteStationsInteractor: FavoriteStationsInteractor) :
    BaseViewModel() {
    private val _getStationsListData: MutableLiveData<MutableList<StationItemLocal>> by lazy { MutableLiveData<MutableList<StationItemLocal>>() }
    val getStationsListData: LiveData<MutableList<StationItemLocal>> = _getStationsListData

    fun getFavoriteStationList() {
        viewModelScope.launch(Dispatchers.Unconfined) {
            withContext(Dispatchers.Main) {
                _getStationsListData.value = favoriteStationsInteractor.getAllStationDataLocalDB()
            }
        }
    }

    fun removeFavoriteItem(itemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteStationsInteractor.removeStationDataLocalDB(itemId)
        }
    }
}