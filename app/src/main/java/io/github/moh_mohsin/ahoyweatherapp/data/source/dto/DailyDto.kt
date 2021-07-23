package io.github.moh_mohsin.ahoyweatherapp.data.source.dto

import androidx.room.Embedded

data class DailyDto(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val moonrise: Int,
    val moonset: Int,
    val moonPhase: Double,
    @Embedded(prefix = "temp_")  val temp: TempDto,
    @Embedded(prefix = "feelsLike_")  val feelsLike: FeelsLikeDto,
    val pressure: Int,
    val humidity: Int,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double,
    @Embedded val weather: List<WeatherDescDto>, //TODO
    val clouds: Int,
    val pop: Double,
    val uvi: Double,
    val rain: Double
)