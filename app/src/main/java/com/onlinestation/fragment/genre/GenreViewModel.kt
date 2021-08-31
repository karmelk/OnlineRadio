package com.onlinestation.fragment.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.onlinestation.appbase.viewmodel.BaseViewModel
import com.onlinestation.domain.interactors.GenreInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.onlinestation.data.entities.request.GenderItem
import kotlinx.coroutines.launch
import com.onlinestation.data.entities.Result
import com.onlinestation.data.util.NO_INTERNET_CONNECTION
import com.onlinestation.domain.entities.StationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GenreViewModel(private val genreInteractor: GenreInteractor) : BaseViewModel() {

    private val _getGenderData: MutableStateFlow<List<GenderItem>?> by lazy { MutableStateFlow(null) }
    val getGenderData = _getGenderData.asStateFlow()

    private val _errorGenderData: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }
    val errorGenderData = _errorGenderData.asStateFlow()

    fun getGenreList() {
        viewModelScope.launch() {
            when (val userData = genreInteractor()) {
                is Result.Success -> {
                    _getGenderData.value = userData.data
                    if (_getGenderData.value.isNullOrEmpty()) {
                        showEmptyData()
                    }
                }
                is Result.Error -> {
                    if (userData.errors.errorCode == NO_INTERNET_CONNECTION) {
                        showNotNetworksConnection()
                    }
                    _errorGenderData.value = Unit
                    if (_getGenderData.value.isNullOrEmpty()) {
                        showEmptyData()
                    }
                }
            }
        }
    }
}