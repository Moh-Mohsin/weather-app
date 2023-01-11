package io.github.moh_mohsin.ahoyweatherapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import io.github.moh_mohsin.ahoyweatherapp.data.worker.DailyWeatherNotificationWorker
import org.joda.time.DateTime
import org.joda.time.Duration
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        setupDailyWeatherNotificationJob()
    }

    private fun setupDailyWeatherNotificationJob() {
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

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }
}