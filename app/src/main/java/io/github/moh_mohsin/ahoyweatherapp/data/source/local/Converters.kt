package io.github.moh_mohsin.ahoyweatherapp.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherDetailDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.DailyDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherDescDto


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
    fun weatherDtoToJson(weatherDtos: List<WeatherDescDto>) : String {
        return Gson().toJson(weatherDtos)
    }

    @TypeConverter
    fun weatherDtoformJson(weatherDtosString: String) : List<WeatherDescDto> {
        return Gson().fromJson(weatherDtosString, object : TypeToken<List<WeatherDescDto>>() {}.type)
    }


    @TypeConverter
    fun currentDtosToJson(currentDtos: List<WeatherDetailDto>) : String {
        return Gson().toJson(currentDtos)
    }

    @TypeConverter
    fun currentDtosFormJson(currentDtosString: String) : List<WeatherDetailDto> {
        return Gson().fromJson(currentDtosString, object : TypeToken<List<WeatherDetailDto>>() {}.type)
    }
}