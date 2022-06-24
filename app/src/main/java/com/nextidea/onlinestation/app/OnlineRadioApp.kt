package com.nextidea.onlinestation.app

import android.app.Application
import com.nextidea.onlinestation.BuildConfig
import com.nextidea.onlinestation.data.di.apiModule
import com.nextidea.onlinestation.data.di.databaseModule
import com.nextidea.onlinestation.data.di.repositoryModule
import com.nextidea.onlinestation.domain.di.interactorModule
import com.nextidea.onlinestation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class OnlineRadioApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@OnlineRadioApp)
            modules(modules)
        }
    }

    private val modules = listOf(
            apiModule,
            databaseModule,
            repositoryModule,
            interactorModule,
            viewModelModule
    )
}