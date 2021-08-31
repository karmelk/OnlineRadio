package com.onlinestation.fragment.searchradios.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.onlinestation.appbase.viewmodel.BaseViewModel
import com.onlinestation.data.entities.Constants.Companion.errorDataEmpty
import com.onlinestation.data.entities.Constants.Companion.errorNotBalanceCode
import com.onlinestation.data.entities.Constants.Companion.notFountIndexExaction
import com.onlinestation.domain.interactors.SearchStationInteractor
import com.onlinestation.domain.interactors.AddStationDBInteractor
import com.onlinestation.data.entities.Result
import kotlinx.coroutines.Dispatchers
import com.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val searchStationInteractor: SearchStationInteractor,
    private val addStationDBInteractor: AddStationDBInteractor
) : BaseViewModel() {

    private val _errorAddStationLD: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorAddStationLD= _errorAddStationLD.asStateFlow()

    private val _errorNotBalanceLD: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorNotBalanceLD= _errorNotBalanceLD.asStateFlow()

    private val _getSearchStationLD: MutableStateFlow<List<StationItem>?> by lazy { MutableStateFlow(null) }
    val getSearchStationLD= _getSearchStationLD.asStateFlow()

    fun searchStation(searchKeyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = searchStationInteractor(searchKeyword)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getSearchStationLD.value = result.data
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    when (result.errors.errorCode) {
                        errorDataEmpty -> {
                          //  _getSearchStationLD.value = result.da
                            showEmptyData()
                        }
                    }
                }
            }
        }
    }

    fun addRemoveStation(item: StationItem) {
        viewModelScope.launch {

            when (val result =
                addStationDBInteractor(item, _getSearchStationLD.value)) {
                is Result.Success -> {
                    _getSearchStationLD.value = result.data?.first
                    addedOrRemovedIsFavorite(result.data?.second)
                }
                is Result.Error -> {
                    when (result.errors.errorCode) {
                        errorNotBalanceCode -> {
                            _errorNotBalanceLD.value = Unit
                        }
                        notFountIndexExaction -> {
                            _errorAddStationLD.value = Unit
                        }
                    }
                }
            }

        }
    }


}