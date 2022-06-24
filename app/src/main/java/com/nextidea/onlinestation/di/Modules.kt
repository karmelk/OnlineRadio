package com.nextidea.onlinestation.di

import com.nextidea.onlinestation.activity.ShearViewModel
import com.nextidea.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import com.nextidea.onlinestation.fragment.stationsbygenderId.viewmodel.StationListByGenreIdViewModel
import com.nextidea.onlinestation.fragment.genre.GenreViewModel
import com.nextidea.onlinestation.fragment.topstations.TopStationViewModel
import com.nextidea.onlinestation.fragment.searchradios.viewmodel.SearchViewModel
import com.nextidea.onlinestation.fragment.settings.viewmodel.SettingsViewModel
import com.nextidea.onlinestation.activity.MainViewModel
import com.nextidea.onlinestation.service.PlayingRadioLibrary
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
