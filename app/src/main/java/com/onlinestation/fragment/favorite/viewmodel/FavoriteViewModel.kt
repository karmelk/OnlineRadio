package com.onlinestation.fragment.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.onlinestation.appbase.viewmodel.BaseViewModel
import com.onlinestation.data.entities.Constants.Companion.notFountIndexExaction
import com.onlinestation.domain.interactors.AddFavoriteStationsDBInteractor
import com.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.onlinestation.data.entities.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoriteViewModel(
    private val addFavoriteStationsDBInteractor: AddFavoriteStationsDBInteractor
) : BaseViewModel() {

    private val _getStationsListData: MutableStateFlow<List<StationItem>?> by lazy { MutableStateFlow(null) }
    val getStationsListData = _getStationsListData.asStateFlow()

    private val _errorAddStationLD: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorAddStationLD = _errorAddStationLD.asStateFlow()


    init {
        getFavoriteStationList()
    }

    private fun getFavoriteStationList() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val result=addFavoriteStationsDBInteractor.getAllStationDataLocalDB()
                _getStationsListData.value = result
                if(result.isNullOrEmpty()){
                    showEmptyData()
                }
            }
        }
    }

    fun addRemoveStationItem(item: StationItem) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result =  addFavoriteStationsDBInteractor.addRemoveStationFromDB(item)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getStationsListData.value = result.data?.first
                    addedOrRemovedIsFavorite(result.data?.second)
                    if(result.data?.first.isNullOrEmpty()){
                        showEmptyData()
                    }
                }
                is Result.Error -> {
                    when (result.errors.errorCode) {
                        notFountIndexExaction -> {
                            _errorAddStationLD.value = Unit
                        }
                    }
                }
            }
        }
    }
}