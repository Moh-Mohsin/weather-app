package io.github.moh_mohsin.ahoyweatherapp.data.source

import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    fun getWeather(lat: Double, lon: Double): Flow<WeatherInfoDto?>

    suspend fun saveWeather(weatherInfoDto: WeatherInfoDto)

}