package io.github.moh_mohsin.ahoyweatherapp.domain

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {

    operator fun invoke(lat: Double, lon: Double): Flow<Result<WeatherInfo>> {
        return weatherRepository.getWeather(lat, lon)
    }
}