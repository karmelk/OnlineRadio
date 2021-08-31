package com.onlinestation.domain.di


import com.onlinestation.data.entities.Result
import com.onlinestation.domain.entities.StationItem
import com.onlinestation.domain.interactors.*
import com.onlinestation.domain.usecases.*
import org.koin.dsl.module

val interactorModule = module {
    single<GenreInteractor> { GenreUseCase(get()) }
    single<TopStationInteractor> { TopStationUseCase(get(),get()) }
    factory<StationListByGenreIdInteractor> { StationListByGenreIdUseCase(get(),get()) }
    single<AddFavoriteStationsDBInteractor> { AddFavoriteStationsDBUseCase(get()) }
    single<SettingsInteractor> { SettingsUseCase(get()) }
    single<SearchStationInteractor> { SearchStationUseCase(get(), get()) }
    single<MainActivityInteractor> { MainActivityUseCase(get(),get()) }
    single<AddStationDBInteractor> { AddStationDBUseCase(get()) }
}
