package com.onlinestation.fragment.secondarygenre.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.BaseViewModel
import com.onlinestation.domain.interactors.SecondaryGenreInteractor
import com.onlinestation.entities.responcemodels.gendermodels.SecondaryGenreItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.onlinestation.entities.Result
import kotlinx.coroutines.launch

class SecondaryGenreViewModel(private val secondaryGenreInteractor: SecondaryGenreInteractor) :
    BaseViewModel() {
    val getSecondaryGenreData: MutableLiveData<MutableList<SecondaryGenreItem>> by lazy { MutableLiveData<MutableList<SecondaryGenreItem>>() }
    fun getSecondaryGenderList(parentId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userData = secondaryGenreInteractor.getSecondaryGenreListData(parentId)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    getSecondaryGenreData.value = userData.data
                }
            }
        }
    }
}