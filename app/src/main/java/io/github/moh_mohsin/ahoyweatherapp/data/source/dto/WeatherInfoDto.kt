package io.github.moh_mohsin.ahoyweatherapp.data.source.dto

import androidx.room.Embedded
import androidx.room.Entity

@Entity(primaryKeys = ["lat", "lon"])
data class WeatherInfoDto(
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezoneOffset: Int,
    @Embedded(prefix = "current_") var current: WeatherDetailDto,
    var daily: List<DailyDto>,
    var hourly: List<WeatherDetailDto>,
){
//    constructor(): this(0.0, 0.0, "", 0, null!!, null!!)
}