package io.github.moh_mohsin.ahoyweatherapp.data.repository

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeather(lat: Double, lon: Double): Flow<Result<WeatherInfo>>

    fun refreshWeather(lat: Double, lon: Double)

    suspend fun cleanCache()
}