package io.github.moh_mohsin.ahoyweatherapp.data.model

import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.TempScale

data class WeatherInfo(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Int,
    val current: WeatherDetail,
    val daily: List<Daily>,
    var hourly: List<WeatherDetail>,
    val tempScale: TempScale = TempScale.METRIC
)