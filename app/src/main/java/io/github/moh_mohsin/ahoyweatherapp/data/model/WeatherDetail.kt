package io.github.moh_mohsin.ahoyweatherapp.data.model

import java.util.*

data class WeatherDetail(
    val dt: Date,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double,
    val weather: List<WeatherDesc>,
)