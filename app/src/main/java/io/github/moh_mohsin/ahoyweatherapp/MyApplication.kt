package io.github.moh_mohsin.ahoyweatherapp

import android.app.Application
import androidx.room.Room
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.AppDatabase
import io.github.moh_mohsin.ahoyweatherapp.di.appDependencies
import io.github.moh_mohsin.ahoyweatherapp.di.cityFeatureDependencies
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber

class MyApplication : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        bind<AppDatabase>() with singleton {
            Room.databaseBuilder(
                //TODO: replace with databaseBuilder
                applicationContext,
                AppDatabase::class.java,
                "ahoy-weather",
            )
//                .createFromAsset("database/ahoy-weather.db")
                .build()
        }
        import(appDependencies)
        import(cityFeatureDependencies)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}