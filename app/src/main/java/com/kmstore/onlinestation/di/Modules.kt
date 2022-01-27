package com.kmstore.onlinestation.di

import com.kmstore.onlinestation.activity.ShearViewModel
import com.kmstore.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import com.kmstore.onlinestation.fragment.stationsbygenderId.viewmodel.StationListByGenreIdViewModel
import com.kmstore.onlinestation.fragment.genre.GenreViewModel
import com.kmstore.onlinestation.fragment.topstations.TopStationViewModel
import com.kmstore.onlinestation.fragment.searchradios.viewmodel.SearchViewModel
import com.kmstore.onlinestation.fragment.settings.viewmodel.SettingsViewModel
import com.kmstore.onlinestation.activity.MainViewModel
import com.kmstore.onlinestation.service.PlayingRadioLibrary
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
