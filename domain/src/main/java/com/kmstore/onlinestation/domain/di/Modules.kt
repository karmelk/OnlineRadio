package com.kmstore.onlinestation.domain.di

import com.kmstore.onlinestation.domain.interactors.*
import com.kmstore.onlinestation.domain.usecases.*
import org.koin.dsl.module

val interactorModule = module {
    factory<GenreInteractorUseCase> { GenreUseCaseImpl(get()) }
    factory<TopStationInteractorUseCase> { TopStationUseCaseImpl(get(),get()) }
    factory<StationListByGenreIdInteractorUseCase> { StationListByGenreIdUseCaseImpl(get(),get()) }
    single<AddFavoriteStationsDBUseCase> { AddFavoriteStationsDBUseCaseImpl(get()) }
    single<SettingsInteractorUseCase> { SettingsUseCaseImpl(get()) }
    single<SearchStationInteractorUseCase> { SearchStationUseCaseImpl(get(), get()) }
    single<MainActivityInteractorUseCase> { MainActivityUseCaseImpl(get(),get()) }
    single<AddStationDBInteractorUseCase> { AddStationDBUseCaseImpl(get()) }
}


