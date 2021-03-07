package com.onlinestation.fragment.searchradios.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.viewmodel.BaseViewModel
import com.onlinestation.domain.interactors.SearchStationInteractor
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(private val searchStationInteractor: SearchStationInteractor) :
    BaseViewModel() {

    private val _successAddStationLD by lazy { MutableLiveData<StationItem>() }
    private val _errorAddStationLD by lazy { MutableLiveData<StationItem>() }
    private val _errorNotBalanceLD by lazy { MutableLiveData<StationItem>() }
    private val _getSearchStationLD by lazy { MutableLiveData<MutableList<StationItem>>() }
    val successAddStationLD get() = _successAddStationLD
    val errorAddStationLD get() = _errorAddStationLD
    val errorNotBalanceLD get() = _errorNotBalanceLD
    val getSearchStationLD get() = _getSearchStationLD

    fun searchStation(searchKeyword: String, genre: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userData = searchStationInteractor.searchStationListData(searchKeyword, genre)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                   // _getSearchStationLD.value = userData.data
                }
                is Result.Error -> {

                }
            }
        }
    }

    fun addStationLocalDB(item: StationItem) {
        viewModelScope.launch(Dispatchers.IO) {
           /* searchStationInteractor.addStationDataLocalDB(item).collect { data ->
                when (data) {
                    is Result.Success -> withContext(Dispatchers.Main) {
                        _successAddStationLD.value = data.data
                    }
                    is Result.Error -> withContext(Dispatchers.Main) {
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
            }*/
        }
    }

    fun removeStationLocalDB(itemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            searchStationInteractor.removeStationDataLocalDB(itemId)
        }
    }

    fun getBalanceData(): OwnerUserBalance? = searchStationInteractor.getBalanceData()
}