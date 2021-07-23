package io.github.moh_mohsin.ahoyweatherapp.data.source.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore

@Entity(primaryKeys = ["lat", "lon"])
data class WeatherInfoDto(
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezoneOffset: Int,
    @Embedded(prefix = "current_") var current: CurrentDto,
    var daily: List<DailyDto>
){
//    constructor(): this(0.0, 0.0, "", 0, null!!, null!!)
}