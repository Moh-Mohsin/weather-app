package io.github.moh_mohsin.ahoyweatherapp.data.source.dto

data class DailyDto(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val moonrise: Int,
    val moonset: Int,
    val moonPhase: Double,
    val temp: TempDto,
    val feelsLike: FeelsLikeDto,
    val pressure: Int,
    val humidity: Int,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double,
    val weather: List<WeatherDto>,
    val clouds: Int,
    val pop: Double,
    val uvi: Double,
    val rain: Double
)