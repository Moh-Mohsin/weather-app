package io.github.moh_mohsin.ahoyweatherapp.data.model

data class WeatherInfo(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Int,
    val current: WeatherDetail,
    val daily: List<Daily>,
    var hourly: List<WeatherDetail>,
    val tempUnit: TempUnit = TempUnit.CELSIUS
)