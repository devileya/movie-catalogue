package com.devileya.moviecatalogue

import android.app.Application
import com.devileya.moviecatalogue.domain.module.appModules
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by Arif Fadly Siregar 2019-10-31.
 */
class MovieCatalogueApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }

        startKoin {
            androidContext(this@MovieCatalogueApplication)
            modules(appModules)
        }
    }
}