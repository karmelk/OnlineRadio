package com.onlinestation.di

import com.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import com.onlinestation.fragment.stationsbygenreid.viewmodel.StationListByGenreIdViewModel
import com.onlinestation.fragment.primarygenre.viewmodel.PrimaryGenderViewModel
import com.onlinestation.fragment.randomradios.viewmodel.RandomStationViewModel
import com.onlinestation.fragment.searchradios.viewmodel.SearchViewModel
import com.onlinestation.fragment.secondarygenre.viewmodel.SecondaryGenreViewModel
import com.onlinestation.fragment.settings.viewmodel.SettingsViewModel
import com.onlinestation.onlineradioapp.MainViewModel
import com.onlinestation.service.RadioService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PrimaryGenderViewModel(get()) }
    viewModel { RandomStationViewModel(get()) }
    viewModel { SecondaryGenreViewModel(get()) }
    viewModel { StationListByGenreIdViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { MainViewModel(get(),get()) }
    viewModel { SearchViewModel(get()) }
}
