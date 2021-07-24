package io.github.moh_mohsin.ahoyweatherapp

import android.app.Application
import androidx.room.Room
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.AppDatabase
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.AppPreference
import io.github.moh_mohsin.ahoyweatherapp.data.worker.DailyWeatherNotificationWorker
import io.github.moh_mohsin.ahoyweatherapp.di.cityFeatureDependencies
import io.github.moh_mohsin.ahoyweatherapp.di.weatherFeatureDependencies
import org.joda.time.DateTime
import org.joda.time.Duration
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber
import java.util.concurrent.TimeUnit


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

        setupDailyWeatherNotificationJob()
    }

    fun setupDailyWeatherNotificationJob() {
        val notificationHour = 6

        val plusDay = if (DateTime.now().hourOfDay < notificationHour) 1 else 0
        val delay =
            Duration(
                DateTime.now(),
                DateTime.now().withTimeAtStartOfDay().plusDays(plusDay).plusHours(notificationHour)
            ).standardMinutes

        val tag = "daily_weather_notification"
        val workRequest = PeriodicWorkRequest.Builder(
            DailyWeatherNotificationWorker::class.java,
            24,
            TimeUnit.HOURS,
            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
            TimeUnit.MILLISECONDS
        )
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .addTag(tag)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                tag,
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )

        // NOTE: uncomment this to try an immediate notification

//        val oneTimeRequest =
//            OneTimeWorkRequestBuilder<DailyWeatherNotificationWorker>()
//                .build()
//        WorkManager.getInstance(applicationContext)
//            .enqueue(oneTimeRequest)
    }
}