package io.github.moh_mohsin.ahoyweatherapp.data.repository

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import kotlinx.coroutines.flow.StateFlow

interface WeatherRepository {

    suspend fun getWeather(lat: Double, lon: Double): StateFlow<Result<WeatherInfo>>
}