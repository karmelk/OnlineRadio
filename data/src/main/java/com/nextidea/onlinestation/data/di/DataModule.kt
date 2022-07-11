package com.nextidea.onlinestation.data.di

import android.app.Application
import androidx.room.Room
import com.nextidea.onlinestation.data.BuildConfig
import com.nextidea.onlinestation.data.dataservice.apiservice.AllApiService
import com.nextidea.onlinestation.data.dataservice.sqlservice.AppDatabase
import com.nextidea.onlinestation.data.datastore.*
import com.nextidea.onlinestation.data.repository.*
import com.nextidea.onlinestation.data.util.HeaderInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@ComponentScan("com.nextidea.onlinestation.data")
class DataModule() {

    @Single
    fun providesInterceptor(): HeaderInterceptor =
        HeaderInterceptor()

    @Single
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Single
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Single
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BuildConfig.API_URL)
        .client(okHttpClient)
        .build()

    @Single
    fun provideApiService(retrofit: Retrofit): AllApiService =
        retrofit.create(AllApiService::class.java)


    @Single
    fun provideFavoriteDao(db: AppDatabase) = db.favoriteDao()

    @Single
    fun provideBalanceDao(db: AppDatabase) = db.balanceDao()

    @Single
    fun provideGenreDao(db: AppDatabase) = db.genreDao()

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "RadioDB")
            //.fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}




