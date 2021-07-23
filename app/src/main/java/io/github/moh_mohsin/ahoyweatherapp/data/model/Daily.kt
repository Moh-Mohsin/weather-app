package io.github.moh_mohsin.ahoyweatherapp.data.model

import java.util.*

data class Daily(
    val dt: Date,
    val sunrise: Int,
    val sunset: Int,
    val moonrise: Int,
    val moonset: Int,
    val moonPhase: Double,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double,
    val weather: List<WeatherDesc>,
    val clouds: Int,
    val pop: Double,
    val uvi: Double,
    val rain: Double
)