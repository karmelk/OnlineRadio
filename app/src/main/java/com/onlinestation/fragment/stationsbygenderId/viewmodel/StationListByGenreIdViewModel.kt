package com.onlinestation.fragment.stationsbygenderId.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.viewmodel.BaseViewModel
import com.onlinestation.domain.interactors.SqlRoomInteractor
import com.onlinestation.domain.interactors.StationListByGenreIdInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import kotlinx.coroutines.launch

class StationListByGenreIdViewModel(
    private val stationListByGenreIdInteractor: StationListByGenreIdInteractor,
    private val sqlRoomInteractor: SqlRoomInteractor
) : BaseViewModel() {

    private val _errorNotBalanceLD by lazy { MutableLiveData<Unit>() }
    val errorNotBalanceLD get() = _errorNotBalanceLD
    private val _getStationsListLD by lazy { MutableLiveData<List<StationItem>>() }
    val getStationsListLD: LiveData<List<StationItem>> get() = _getStationsListLD
    private val _errorAddStationLD by lazy { MutableLiveData<Unit>() }
    val errorAddStationLD get() = _errorAddStationLD
    private val _errorStationsListLD by lazy { MutableLiveData<Unit>() }
    val errorStationsListLD: LiveData<Unit> get() = _errorStationsListLD

    fun getStationsByGenreIdList(genreId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            when (val userData =
                stationListByGenreIdInteractor.getStationListByGenreIdData(genreId)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getStationsListLD.value = userData.data

                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    _errorStationsListLD.value = Unit
                }
            }
        }
    }

    fun addRemoveStationLocalDB(item: StationItem) {
        viewModelScope.launch(Dispatchers.Main) {
            when (val data =
                sqlRoomInteractor.addRemoveStationFromDB(item, _getStationsListLD.value)) {
                is Result.Success -> {
                    _getStationsListLD.value = data.data
                }
                is Result.Error -> {
                    when (data.errors.errorCode) {
                        Constants.errorNotBalanceCode -> {
                            _errorNotBalanceLD.value = Unit
                        }
                        Constants.notFountIndexExaction -> {
                            _errorAddStationLD.value = Unit
                        }
                    }
                }
            }
        }
    }


}

