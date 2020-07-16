package com.onlinestation.onlineradioapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kmworks.appbase.BaseViewModel
import com.onlinestation.domain.interactors.MainActivityInteractor
import com.onlinestation.domain.interactors.SettingsInteractor
import com.onlinestation.entities.Result
import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.utils.lOADrADIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val mainActivityInteractor: MainActivityInteractor
) : BaseViewModel() {
    private val _primaryGenreDB by lazy { MutableLiveData<MutableList<GenderItem>>() }
    val primaryGenreDB: LiveData<MutableList<GenderItem>> get() = _primaryGenreDB
    fun getBalance(): OwnerUserBalance? = settingsInteractor.getBalanceData()
    fun setDefaultBalance(defaultId: Int,balanceCount:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsInteractor.rewardBalanceDB(defaultId , balanceCount).collect {}
        }
    }

    fun getGenderListDB() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userData = mainActivityInteractor.getPrimaryGenreDB()) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _primaryGenreDB.value = userData.data
                }
            }
        }
    }

    fun loadRadio(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val player=  lOADrADIO(context)
            player.loadRadio()
        }
    }
}