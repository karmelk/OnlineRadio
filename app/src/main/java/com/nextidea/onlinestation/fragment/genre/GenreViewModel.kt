package com.nextidea.onlinestation.fragment.genre

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nextidea.onlinestation.appbase.viewmodel.BaseViewModel
import com.nextidea.onlinestation.data.dataservice.source.CountryPagingSource
import com.nextidea.onlinestation.domain.interactors.GenreInteractorUseCase
import com.nextidea.onlinestation.data.entities.request.GenderItem
import kotlinx.coroutines.launch
import com.nextidea.onlinestation.data.entities.DataResult
import com.nextidea.onlinestation.data.entities.gendermodels.ResponseGender
import com.nextidea.onlinestation.data.repository.GenreRepository
import com.nextidea.onlinestation.data.util.NO_INTERNET_CONNECTION
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

@KoinViewModel
class GenreViewModel(private val genreUseCase: GenreInteractorUseCase, private val countryPagingSource: GenreRepository) : BaseViewModel() {

    private val _getGenderData: MutableStateFlow<List<GenderItem>?> by lazy { MutableStateFlow(null) }
    val getGender = _getGenderData.asStateFlow()

    private val _errorGenderData by lazy { MutableSharedFlow<Unit>() }
    val errorGenderData = _errorGenderData.asSharedFlow()

    private val _isLastPage by lazy { MutableSharedFlow<Boolean>() }
    val isLastPage = _isLastPage.asSharedFlow()

    private val _query:MutableStateFlow<List<GenderItem>?> by lazy { MutableStateFlow(null) }

    val data = _query
        .map{ newPager() }
        .flatMapLatest{ it.flow }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty()).cachedIn(viewModelScope)

    private fun newPager(): Pager<Int, GenderItem> {
        return Pager(PagingConfig(30, enablePlaceholders = false, prefetchDistance = 10)) {
            countryPagingSource.getGenreListDataByPaging()
        }
    }


    fun getGenreList(update: Boolean = false) {
//        viewModelScope.launch() {
//
//            when (val result = genreUseCase(update, _getGenderData.value)) {
//                is DataResult.Success -> {
//                    result.data?.let {
//                        result.data?.let {
//                            _getGenderData.value = it.first
//                            _isLastPage.emit(it.second)
//                        }
//                    }
//
//                }
//                is DataResult.Error -> {
//                    if (result.errors.errorCode == NO_INTERNET_CONNECTION) {
//                        showNotNetworksConnection()
//                    }
//                    _errorGenderData.emit(Unit)
//                }
//            }
//            isShowEmptyData(_getGenderData.value.isNullOrEmpty())
//            swipeRefreshState(false)
//        }
    }
}