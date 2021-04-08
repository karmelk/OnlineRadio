package com.onlinestation.fragment.searchradios.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.utils.Constants.Companion.errorDataEmpty
import com.kmworks.appbase.viewmodel.BaseViewModel
import com.onlinestation.domain.interactors.SearchStationInteractor
import com.onlinestation.domain.interactors.SqlRoomInteractor
import kotlinx.coroutines.Dispatchers
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val searchStationInteractor: SearchStationInteractor,
    private val sqlRoomInteractor: SqlRoomInteractor
) : BaseViewModel() {

    private val _errorAddStationLD by lazy { MutableLiveData<Unit>() }
    val errorAddStationLD get() = _errorAddStationLD
    private val _errorNotBalanceLD by lazy { MutableLiveData<Unit>() }
    val errorNotBalanceLD get() = _errorNotBalanceLD
    private val _getSearchStationLD by lazy { MutableLiveData<List<StationItem>>() }
    val getSearchStationLD get() = _getSearchStationLD

    fun searchStation(searchKeyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = searchStationInteractor.searchStationListData(searchKeyword)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getSearchStationLD.value = result.data
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    when (result.errors.errorCode) {
                        errorDataEmpty -> {
                            _getSearchStationLD.value = result.errors.errorBody
                            showEmptyData()
                        }
                    }
                }
            }
        }
    }

    fun addStationLocalDB(item: StationItem) {
        viewModelScope.launch(Dispatchers.IO) {

            when (val data =
                sqlRoomInteractor.addRemoveStationFromDB(item, _getSearchStationLD.value)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getSearchStationLD.value = data.data
                }
                is Result.Error -> withContext(Dispatchers.Main) {
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