package io.github.moh_mohsin.ahoyweatherapp.data.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import io.github.moh_mohsin.ahoyweatherapp.MyApplication
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.getOrNull
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import timber.log.Timber

class DailyWeatherNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    init {
        createNotificationChannel()
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {

            (applicationContext as MyApplication)
            val weatherRepository by (applicationContext as KodeinAware).kodein.instance<WeatherRepository>()
            Timber.d("################ doWork #############")
            val permission = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (permission == PackageManager.PERMISSION_GRANTED) {
                Timber.d("location granted")
                LocationServices.getFusedLocationProviderClient(applicationContext).lastLocation.addOnSuccessListener {
                    Timber.d("last location: ${it?.latitude}, ${it?.longitude}")
                    it?.let { location ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val result = weatherRepository.getWeatherOnline(
                                location.latitude,
                                location.longitude
                            )
                            Timber.d("weather result: $result")
                            result.getOrNull()?.let { weatherInfo ->
                                createWeatherNotification(weatherInfo)
                            }
                        }
                    }
                }
            } else {
                Timber.d("location denied")
            }
            Result.success()
        }
    }

    private fun createWeatherNotification(weatherInfo: WeatherInfo) {
        val current = weatherInfo.current
        val builder = NotificationCompat.Builder(applicationContext, WEATHER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) //TODO: load weather icon here
            .setContentTitle(applicationContext.getString(R.string.todays_weather))
            .setContentText("${current.temp}°  |  ${current.weather.firstOrNull()?.description ?: ""}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(applicationContext).notify(WEATHER_NOTIFICATION_ID, builder)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = applicationContext.getString(R.string.weather_channel_name)
            val descriptionText = applicationContext.getString(R.string.weather_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(WEATHER_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val WEATHER_NOTIFICATION_ID = 1
        const val WEATHER_CHANNEL_ID = "WEATHER_CHANNEL_ID"

    }
}