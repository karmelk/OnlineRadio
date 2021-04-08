package com.onlinestation.domain.di


import com.onlinestation.domain.interactors.*
import com.onlinestation.domain.usecases.*
import org.koin.dsl.module

val interactorModule = module {
    single<GenreInteractor> { GenreUseCase(get(),get()) }
    single<TopStationInteractor> { TopStationUseCase(get(),get()) }
    factory<StationListByGenreIdInteractor> { StationListByGenreIdUseCase(get(),get()) }
    single<FavoriteStationsInteractor> { FavoriteStationsUseCase(get()) }
    single<SettingsInteractor> { SettingsUseCase(get()) }
    single<SearchStationInteractor> { SearchStationUseCase(get(), get()) }
    single<MainActivityInteractor> { MainActivityUseCase(get(),get()) }
    single<PlayStationInteractor> { PlayStationUseCase() }
    single<SqlRoomInteractor> { SqlRoomUseCase(get()) }
}
