package io.github.moh_mohsin.ahoyweatherapp.data.repository

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeather(lat: Double, lon: Double): Flow<Result<WeatherInfo>>

    suspend fun getWeatherOnline(lat: Double, lon: Double): Result<WeatherInfo>

    suspend fun cleanCache()
}