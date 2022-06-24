package com.nextidea.onlinestation.fragment.searchradios.viewmodel

import androidx.lifecycle.viewModelScope
import com.nextidea.onlinestation.appbase.viewmodel.BaseViewModel
import com.nextidea.onlinestation.data.entities.Constants.Companion.errorDataEmpty
import com.nextidea.onlinestation.data.entities.Constants.Companion.errorNotBalanceCode
import com.nextidea.onlinestation.data.entities.Constants.Companion.notFountIndexExaction
import com.nextidea.onlinestation.domain.interactors.SearchStationInteractorUseCase
import com.nextidea.onlinestation.domain.interactors.AddStationDBInteractorUseCase
import com.nextidea.onlinestation.data.entities.DataResult
import kotlinx.coroutines.Dispatchers
import com.nextidea.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val searchStationInteractorUseCase: SearchStationInteractorUseCase,
    private val addStationDBInteractorUseCase: AddStationDBInteractorUseCase
) : BaseViewModel() {

    private val _errorAddStationLD by lazy { MutableSharedFlow<Unit>() }
    val errorAddStationLD = _errorAddStationLD.asSharedFlow()

    private val _errorNotBalanceLD by lazy { MutableSharedFlow<Unit>() }
    val errorNotBalanceLD = _errorNotBalanceLD.asSharedFlow()

    private val _getSearchStationLD: MutableStateFlow<List<StationItem>?>  by lazy { MutableStateFlow(null) }
    val getSearchStationLD = _getSearchStationLD.asStateFlow()

    fun searchStation(searchKeyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = searchStationInteractorUseCase(searchKeyword)) {
                is DataResult.Success -> withContext(Dispatchers.Main) {
                    _getSearchStationLD.value = result.data!!
                }
                is DataResult.Error -> withContext(Dispatchers.Main) {
                    when (result.errors.errorCode) {
                        errorDataEmpty -> {
                            //  _getSearchStationLD.value = result.da
                            isShowEmptyData(true)
                        }
                    }
                }
            }
        }
    }

    fun addRemoveStation(item: StationItem) {
        viewModelScope.launch {

            when (val result =
                addStationDBInteractorUseCase(item, _getSearchStationLD.value)) {
                is DataResult.Success -> {
                    _getSearchStationLD.value = result.data?.first!!
                    addedOrRemovedIsFavorite(result.data?.second)
                }
                is DataResult.Error -> {
                    when (result.errors.errorCode) {
                        errorNotBalanceCode -> {
                            _errorNotBalanceLD.emit(Unit)
                        }
                        notFountIndexExaction -> {
                            _errorAddStationLD.emit(Unit)
                        }
                    }
                }
            }

        }
    }


}