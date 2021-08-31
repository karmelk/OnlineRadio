package com.onlinestation.data.di

import android.app.Application
import androidx.room.Room
import com.onlinestation.data.dataservice.sqlservice.AppDatabase
import com.onlinestation.data.dataservice.appservice.PreferenceService
import com.onlinestation.data.dataservice.appservice.PreferenceServiceImpl
import com.onlinestation.data.datastore.*
import com.onlinestation.data.repository.*
import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.entities.Constants.Companion.BASE_API_URL
import com.onlinestation.data.util.HeaderInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val apiModule = module {

    single { Moshi.Builder().build() }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .apply {
                client(
                    OkHttpClient.Builder()
                        .addInterceptor(HeaderInterceptor())
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                        .build()
                )
            }
            .build()
    }
    single<AllApiService> { get<Retrofit>().create(AllApiService::class.java) }
}

val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "RadioDB")
            //.fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    single { provideDatabase(androidApplication()) }
    single { get<AppDatabase>().favoriteDao() }
    single { get<AppDatabase>().balanceDao() }
    single { get<AppDatabase>().genreDao() }
}

val repositoryModule = module {
    single<GenreRepository> { GenreRepositoryImpl(get(),get(),get()) }
    single<TopStationRepository> { TopStationRepositoryImpl(get(),get()) }
    single<PreferenceService> { PreferenceServiceImpl(get()) }
    single<StationListByGenreIdRepository> { StationListByGenreIdRepositoryImpl(get()) }
    single<FavoriteStationsRepository> { FavoriteStationsRepositoryImpl(get()) }
    single<SearchStationRepository> { SearchRepositoryImpl(get()) }
    single<LocalSQLRepository> { LocalSQLRepositoryImpl(get(),get(),get()) }
}


