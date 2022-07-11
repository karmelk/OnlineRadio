package com.nextidea.onlinestation.fragment.genre

import androidx.lifecycle.viewModelScope
import com.nextidea.onlinestation.appbase.viewmodel.BaseViewModel
import com.nextidea.onlinestation.domain.interactors.GenreInteractorUseCase
import com.nextidea.onlinestation.data.entities.request.GenderItem
import kotlinx.coroutines.launch
import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.util.NO_INTERNET_CONNECTION
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class GenreViewModel(private val genreUseCase: GenreInteractorUseCase) : BaseViewModel() {

    private val _getGenderData: MutableStateFlow<List<GenderItem>?> by lazy { MutableStateFlow(null) }
    val getGender = _getGenderData.asStateFlow()

    private val _errorGenderData by lazy { MutableSharedFlow<Unit>() }
    val errorGenderData = _errorGenderData.asSharedFlow()

    private val _isLastPage by lazy { MutableSharedFlow<Boolean>() }
    val isLastPage = _isLastPage.asSharedFlow()

    init {
        getGenreList()
    }

    fun getGenreList(update: Boolean = false) {
        viewModelScope.launch() {

            when (val result = genreUseCase(update, _getGenderData.value)) {
                is DataResult.Success -> {
                    result.data?.let {
                        result.data?.let {
                            _getGenderData.value = it.first
                            _isLastPage.emit(it.second)
                        }
                    }

                }
                is DataResult.Error -> {
                    if (result.errors.errorCode == NO_INTERNET_CONNECTION) {
                        showNotNetworksConnection()
                    }
                    _errorGenderData.emit(Unit)
                }
            }
            isShowEmptyData(_getGenderData.value.isNullOrEmpty())
            swipeRefreshState(false)
        }
    }
}