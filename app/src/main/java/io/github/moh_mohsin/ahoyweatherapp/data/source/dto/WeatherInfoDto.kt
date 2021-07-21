package io.github.moh_mohsin.ahoyweatherapp.data.source.dto

data class WeatherInfoDto(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Int,
    val current: CurrentDto,
    val daily: List<DailyDto>
)