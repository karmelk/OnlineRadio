package com.onlinestation.fragment.genre.viewmodel

import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.viewmodel.BaseViewModel
import com.onlinestation.R
import com.onlinestation.domain.interactors.GenreInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.fragment.stationsbygenderId.StationListByGenreIdFragment
import kotlinx.coroutines.launch

class GenreViewModel(private val genreInteractor: GenreInteractor) : BaseViewModel() {

    init {
        getGenreList()
    }

    private val _getGenderData by lazy { MutableLiveData<List<GenderItem>>() }
    val getGenderData: LiveData<List<GenderItem>> get() = _getGenderData

    fun getGenreList() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userData = genreInteractor.getGenreListData()) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getGenderData.value = userData.data
                }
                is Result.Error -> {
                    Log.i("TAG", "getGenderList: ")
                }
            }
        }
    }



}