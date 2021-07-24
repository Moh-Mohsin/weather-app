package io.github.moh_mohsin.ahoyweatherapp

import android.app.Application
import androidx.room.Room
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.AppDatabase
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.AppPreference
import io.github.moh_mohsin.ahoyweatherapp.di.cityFeatureDependencies
import io.github.moh_mohsin.ahoyweatherapp.di.weatherFeatureDependencies
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber

class MyApplication : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        bind<AppDatabase>() with singleton {
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "ahoy-weather",
            )
                .createFromAsset("database/ahoy-weather.db")
                .build()
        }
        bind<AppPreference>() with singleton { AppPreference(applicationContext) }

        import(weatherFeatureDependencies)
        import(cityFeatureDependencies)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}