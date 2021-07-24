package io.github.moh_mohsin.ahoyweatherapp.domain

import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository

class CleanWeatherCacheUseCase(private val weatherRepository: WeatherRepository) {

    suspend operator fun invoke() {
        weatherRepository.cleanCache()
    }
}