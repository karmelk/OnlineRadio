package com.onlinestation.fragment.primarygenre.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.BaseViewModel
import com.onlinestation.domain.interactors.PrimaryGenreInteractor
import com.onlinestation.entities.responcemodels.gendermodels.PrimaryGenreItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.onlinestation.entities.Result
import kotlinx.coroutines.launch

class PrimaryGenderViewModel(private val primaryGenreInteractor: PrimaryGenreInteractor) :
    BaseViewModel() {

    init {
        getGenderListDB()
    }

    private val _getPrimaryGenreData by lazy { MutableLiveData<MutableList<PrimaryGenreItem>>() }
    val getPrimaryGenreData: LiveData<MutableList<PrimaryGenreItem>> get() = _getPrimaryGenreData
    fun getPrimaryGenderList() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userData = primaryGenreInteractor.getPrimaryGenreListData()) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getPrimaryGenreData.value = userData.data
                }
            }
        }
    }

    private fun getGenderListDB() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userData = primaryGenreInteractor.getPrimaryGenreListDataDB()) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getPrimaryGenreData.value = userData.data
                }
            }
        }
    }
}