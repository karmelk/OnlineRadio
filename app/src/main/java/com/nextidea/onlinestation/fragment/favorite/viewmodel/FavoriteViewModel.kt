package com.nextidea.onlinestation.fragment.favorite.viewmodel

import androidx.lifecycle.viewModelScope
import com.nextidea.onlinestation.appbase.viewmodel.BaseViewModel
import com.nextidea.onlinestation.data.entities.Constants.Companion.notFountIndexExaction
import com.nextidea.onlinestation.domain.interactors.AddFavoriteStationsDBUseCase
import com.nextidea.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.nextidea.onlinestation.data.entities.DataResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FavoriteViewModel(
    private val addFavoriteStationsDBUseCase: AddFavoriteStationsDBUseCase
) : BaseViewModel() {

    private val _getStationsListData: MutableStateFlow<List<StationItem>?> by lazy { MutableStateFlow(null) }
    val getStationsListData = _getStationsListData.asStateFlow()

    private val _errorAddStationLD by lazy { MutableSharedFlow<Unit>() }
    val errorAddStationLD = _errorAddStationLD.asSharedFlow()

    init {
        getFavoriteStationList()
    }

    private fun getFavoriteStationList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = addFavoriteStationsDBUseCase.getAllStationDataLocalDB()
            withContext(Dispatchers.Main) {
                _getStationsListData.value = result
                if (result.isEmpty())
                    isShowEmptyData(true)
                else
                    isShowEmptyData(false)
            }
        }
    }

    fun addRemoveStationItem(item: StationItem) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = addFavoriteStationsDBUseCase.addRemoveStationFromDB(item)) {
                is DataResult.Success -> withContext(Dispatchers.Main) {
                    _getStationsListData.value = result.data?.first
                    addedOrRemovedIsFavorite(result.data?.second)
                    if (result.data?.first.isNullOrEmpty())
                        isShowEmptyData(true)
                    else
                        isShowEmptyData(false)
                }
                is DataResult.Error -> {
                    when (result.errors.errorCode) {
                        notFountIndexExaction -> {
                            _errorAddStationLD.emit(Unit)
                        }
                    }
                }
            }
        }
    }

}