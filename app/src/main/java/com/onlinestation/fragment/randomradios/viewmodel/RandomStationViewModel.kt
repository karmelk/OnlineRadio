package com.onlinestation.fragment.randomradios.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.viewmodel.BaseViewModel
import com.onlinestation.domain.interactors.RandomStationInteractor
import com.onlinestation.entities.Result
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class RandomStationViewModel(private val randomStationInteractor: RandomStationInteractor) :
    BaseViewModel() {
    private val _successAddStationLD by lazy { MutableLiveData<StationItem>() }
    private val _errorAddStationLD by lazy { MutableLiveData<StationItem>() }
    private val _errorNotBalanceLD by lazy { MutableLiveData<StationItem>() }
    private val _getRandomStationLD by lazy { MutableLiveData<MutableList<StationItem>>() }
    val successAddStationLD get() = _successAddStationLD
    val errorAddStationLD get() = _errorAddStationLD
    val errorNotBalanceLD get() = _errorNotBalanceLD
    val getRandomStationLD get() = _getRandomStationLD
    fun getPrimaryGenderList() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userData = randomStationInteractor.getRandomStationListData()) {
                is Result.Success -> withContext(Dispatchers.Main) {
                   // _getRandomStationLD.value = userData.data

                }
            }
        }
    }

    fun addStationLocalDB(item: StationItem) {
        viewModelScope.launch(Dispatchers.IO) {
           /* randomStationInteractor.addStationDataLocalDB(item).collect { data ->
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
            }
        */}
    }

    fun removeStationLocalDB(itemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
         //   randomStationInteractor.removeStationDataLocalDB(itemId)
        }
    }

    fun getBalanceData(): OwnerUserBalance? = randomStationInteractor.getBalanceData()

}