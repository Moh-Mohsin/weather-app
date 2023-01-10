package io.github.moh_mohsin.ahoyweatherapp.data.source.local

import android.content.Context
import androidx.preference.PreferenceManager
import io.github.moh_mohsin.ahoyweatherapp.R
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.TempScale

class AppPreference(context: Context) {

    private val preference = PreferenceManager.getDefaultSharedPreferences(context)

    private val tempScaleKey = context.resources.getString(R.string.temp_scale_key)
//    private val dailyWeatherNotificationKey =
//        context.resources.getString(R.string.daily_weather_notification_key)

    fun getTempScale(): TempScale {
        return TempScale.valueOf(
            preference.getString(
                tempScaleKey,
                TempScale.METRIC.toString()
            )!!
        ) // we know this wont return null
    }

}