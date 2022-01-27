package com.kmstore.onlinestation.app

import android.app.Application
import com.kmstore.onlinestation.data.di.apiModule
import com.kmstore.onlinestation.data.di.databaseModule
import com.kmstore.onlinestation.data.di.repositoryModule
import com.kmstore.onlinestation.domain.di.interactorModule
import com.kmstore.onlinestation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class OnlineRadioApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@OnlineRadioApp)
            modules(modules)
        }
    }

    val modules = listOf(
            apiModule,
            databaseModule,
            repositoryModule,
            interactorModule,
            viewModelModule
    )
}