package io.github.moh_mohsin.ahoyweatherapp.data.source.dto

data class WeatherDetailDto(
    var dt: Int,
    var sunrise: Int,
    var sunset: Int,
    var temp: Double,
    var feelsLike: Double,
    var pressure: Int,
    var humidity: Int,
    var dewPoint: Double,
    var uvi: Double,
    var clouds: Int,
    var visibility: Int,
    var windSpeed: Double,
    var windDeg: Int,
    var windGust: Double,
    var weather: List<WeatherDescDto>
)