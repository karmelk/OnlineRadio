package com.onlinestation.di

import com.onlinestation.activity.ShearViewModel
import com.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import com.onlinestation.fragment.stationsbygenderId.viewmodel.StationListByGenreIdViewModel
import com.onlinestation.fragment.genre.GenreViewModel
import com.onlinestation.fragment.topstations.TopStationViewModel
import com.onlinestation.fragment.searchradios.viewmodel.SearchViewModel
import com.onlinestation.fragment.settings.viewmodel.SettingsViewModel
import com.onlinestation.activity.MainViewModel
import com.onlinestation.service.PlayingRadioLibrary
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GenreViewModel(get()) }
    viewModel { TopStationViewModel(get(),get()) }
    viewModel { StationListByGenreIdViewModel(get(),get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ShearViewModel() }
    viewModel { SearchViewModel(get(),get()) }
    single{ PlayingRadioLibrary() }
}
