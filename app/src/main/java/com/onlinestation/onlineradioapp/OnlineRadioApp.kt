package com.onlinestation.onlineradioapp

import android.app.Application
import com.onlinestation.data.di.apiModule
import com.onlinestation.data.di.databaseModule
import com.onlinestation.data.di.repositoryModule
import com.onlinestation.domain.di.interactorModule
import com.onlinestation.di.viewModelModule
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

    private val modules = listOf(
            apiModule,
            databaseModule,
            repositoryModule,
            interactorModule,
            viewModelModule
    )
}