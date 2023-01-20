package io.github.moh_mohsin.ahoyweatherapp.data.model

import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.TempScale
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

val dailySample = Daily(
    Date(),
    0,
    0,
    0,
    0,
    0.0,
    Temp(10.0, 10.0, 10.0, 10.0, 10.0, 10.0),
    FeelsLike(10.0, 10.0, 10.0, 10.0),
    10,
    10,
    10.0,
    10.0,
    10,
    10.0,
    listOf(WeatherDesc(0, "fine", "fine weather", "")),
    0,
    0.0,
    0.0,
    0.0,
)
val weatherInfo = WeatherInfo(
    0.0,
    0.0,
    "",
    0,
    WeatherDetail(
        Date(),
        0,
        0,
        0.0,
        0.0,
        0,
        0,
        0.0,
        10.0,
        10,
        10,
        10.0,
        10,
        10.0,
        listOf(WeatherDesc(0, "fine", "fine weather", "")),
    ),
    daily = listOf(dailySample),
    hourly = listOf(
        WeatherDetail(
            Date(),

            0,
            0,
            0.0,
            0.0,
            0,
            0,
            0.0,
            10.0,
            10,
            10,
            10.0,
            10,
            10.0,
            listOf(WeatherDesc(0, "fine", "fine weather", "")),
        )
    ),
    tempScale = TempScale.METRIC,
)
