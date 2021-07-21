package io.github.moh_mohsin.ahoyweatherapp.data.model

data class WeatherInfo(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Int,
    val current: Current,
    val daily: List<Daily>,
    val tempUnit: TempUnit = TempUnit.CELSIUS
)