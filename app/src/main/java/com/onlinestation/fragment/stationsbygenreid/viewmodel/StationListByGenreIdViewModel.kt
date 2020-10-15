package com.onlinestation.fragment.stationsbygenreid.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.BaseViewModel
import com.kmworks.appbase.Constants
import com.onlinestation.domain.interactors.StationListByGenreIdInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StationListByGenreIdViewModel(
    private val stationListByGenreIdInteractor: StationListByGenreIdInteractor
) : BaseViewModel() {

    private val _successAddStationLD by lazy { MutableLiveData<StationItemLocal>() }
    private val _errorAddStationLD by lazy { MutableLiveData<StationItemLocal>() }
    private val _errorNotBalanceLD by lazy { MutableLiveData<StationItemLocal>() }
    private val _getStationsListLD by lazy { MutableLiveData<MutableList<StationItemLocal>>() }
    val successAddStationLD get() = _successAddStationLD
    val errorAddStationLD get() = _errorAddStationLD
    val errorNotBalanceLD get() = _errorNotBalanceLD
    val getStationsListLD get() = _getStationsListLD

    fun getStationsByGenreIdList(genreId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            when (val userData =
                stationListByGenreIdInteractor.getStationListByGenreIdData(genreId)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    getStationsListLD.value = userData.data
                }
               /* is Result.Error -> withContext(Dispatchers.Main) {
                    Log.i("ErrorLog", userData.errors.toString())
                    error.value = userData.errors
                }*/
            }
        }
    }

    fun addStationLocalDB(item: StationItemLocal) {
        viewModelScope.launch(Dispatchers.IO) {
            stationListByGenreIdInteractor.addStationDataLocalDB(item).collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is Result.Success -> {
                            _successAddStationLD.value = data.data
                        }
                        is Result.Error -> {
                            when (data.errors.errorCode) {
                                Constants.errorNotBalanceCode -> {
                                    _errorNotBalanceLD.value = data.errors.errorBody
                                }
                                Constants.errorDefaultCode -> {
                                    _errorAddStationLD.value = data.errors.errorBody
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun removeStationLocalDB(itemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            stationListByGenreIdInteractor.removeStationDataLocalDB(itemId)
        }
    }

    fun getBalanceData(): OwnerUserBalance? = stationListByGenreIdInteractor.getBalanceData()

}

