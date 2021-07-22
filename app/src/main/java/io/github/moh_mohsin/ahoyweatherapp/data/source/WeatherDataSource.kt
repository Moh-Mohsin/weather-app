package io.github.moh_mohsin.ahoyweatherapp.data.source

import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto
import kotlinx.coroutines.flow.StateFlow

interface WeatherDataSource {

    suspend fun getWeather(lat: Double, lon: Double): StateFlow<Result<WeatherInfoDto>>

    suspend fun saveWeather(weatherInfoDto: WeatherInfoDto)
}