package io.github.moh_mohsin.ahoyweatherapp.data.source.dto

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)