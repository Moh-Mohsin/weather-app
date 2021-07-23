package io.github.moh_mohsin.ahoyweatherapp.data.source.local

import android.provider.MediaStore.Video
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.DailyDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherDto


class Converters {

    @TypeConverter
    fun dailyDtosToJson(dailyDtos: List<DailyDto>) : String {
        return Gson().toJson(dailyDtos)
    }

    @TypeConverter
    fun dailyDtosFormJson(dailyDtosString: String) : List<DailyDto> {
        return Gson().fromJson(dailyDtosString, object : TypeToken<List<DailyDto>>() {}.type)
    }


    @TypeConverter
    fun toJson(weatherDtos: List<WeatherDto>) : String {
        return Gson().toJson(weatherDtos)
    }

    @TypeConverter
    fun formJson(weatherDtosString: String) : List<WeatherDto> {
        return Gson().fromJson(weatherDtosString, object : TypeToken<List<WeatherDto>>() {}.type)
    }
}