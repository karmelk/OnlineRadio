package com.onlinestation.data.di

import android.app.Application
import androidx.room.Room
import com.kmworks.appbase.utils.Constants.Companion.BASE_API_URL
import com.kmworks.appbase.utils.Constants.Companion.BASE_OWNER_SERVER_API_URL
import com.onlinestation.data.dataservice.apiservice.AllApiService
import com.onlinestation.data.dataservice.sqlservice.AppDatabase
import com.onlinestation.data.dataservices.appservice.PreferenceService
import com.onlinestation.data.dataservices.appservice.PreferenceServiceImpl
import com.onlinestation.data.datastore.*
import com.onlinestation.data.repository.*
import com.onlinestation.data.dataservice.apiservice.OwnerServerApiService
import com.onlinestation.data.util.HeaderInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val apiModule = module {

    single { Moshi.Builder().build() }
    single<Retrofit> (named("Shoutcast")){
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
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_OWNER_SERVER_API_URL)
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

    single<AllApiService> { get<Retrofit>(named("Shoutcast")).create(AllApiService::class.java) }
    single<OwnerServerApiService> { get<Retrofit>().create(OwnerServerApiService::class.java) }
}

val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "RadioDB")
            // .fallbackToDestructiveMigration()
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
    single<RandomStationRepository> { RandomStationRepositoryImpl(get()) }
    single<PreferenceService> { PreferenceServiceImpl(get()) }
    single<StationListByGenreIdRepository> { StationListByGenreIdRepositoryImpl(get()) }
    single<FavoriteStationsRepository> { FavoriteStationsRepositoryImpl(get()) }
    single<SearchStationRepository> { SearchRepositoryImpl(get()) }
    single<LocalSQLRepository> { LocalSQLRepositoryImpl(get(),get(),get()) }

}


