package com.onlinestation.di

import com.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import com.onlinestation.fragment.stationsbygenderId.viewmodel.StationListByGenreIdViewModel
import com.onlinestation.fragment.genre.viewmodel.GenreViewModel
import com.onlinestation.fragment.randomradios.viewmodel.RandomStationViewModel
import com.onlinestation.fragment.searchradios.viewmodel.SearchViewModel
import com.onlinestation.fragment.settings.viewmodel.SettingsViewModel
import com.onlinestation.activity.MainViewModel
import com.onlinestation.service.PlayingRadioLibrary
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GenreViewModel(get()) }
    viewModel { RandomStationViewModel(get()) }
    viewModel { StationListByGenreIdViewModel(get()) }
    viewModel { FavoriteViewModel(get(),get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    single{ PlayingRadioLibrary() }
}
