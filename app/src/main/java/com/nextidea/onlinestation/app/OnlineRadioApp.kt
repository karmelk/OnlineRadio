package com.nextidea.onlinestation.app

import android.app.Application
import com.nextidea.onlinestation.BuildConfig
import com.nextidea.onlinestation.data.di.DataModule
import com.nextidea.onlinestation.di.AppModule
import com.nextidea.onlinestation.domain.DomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module


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
        //databaseModule,
        //apiModule,
        //repositoryModule,
        AppModule().module,
        DomainModule().module,
        DataModule().module,
    )
}