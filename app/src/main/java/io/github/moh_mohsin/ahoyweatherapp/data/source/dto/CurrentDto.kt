package io.github.moh_mohsin.ahoyweatherapp.data.source.dto

data class CurrentDto(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val dewPoint: Double,
    val uvi: Int,
    val clouds: Int,
    val visibility: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double,
    val weather: List<WeatherDto>
)