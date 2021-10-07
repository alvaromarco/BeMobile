package com.amp.bemobile

import android.app.Application
import androidx.multidex.BuildConfig
import com.amp.bemobile.data.core.di.repositoryModule
import com.amp.bemobile.data.core.di.serviceModule
import com.amp.bemobile.domain.core.di.domainModule
import com.amp.bemobile.view.core.di.applicationModule
import com.amp.bemobile.view.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BeMobileApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initTimber()
    }


    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@BeMobileApplication)

            koin.loadModules(listOf(
                applicationModule,
                viewModelModule,
                domainModule,
                repositoryModule,
                serviceModule,
            ))
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}