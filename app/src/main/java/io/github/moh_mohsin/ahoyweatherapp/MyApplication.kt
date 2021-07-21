package io.github.moh_mohsin.ahoyweatherapp

import android.app.Application
import io.github.moh_mohsin.ahoyweatherapp.di.appDependencies
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import timber.log.Timber

class MyApplication: Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(appDependencies)
    }
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}