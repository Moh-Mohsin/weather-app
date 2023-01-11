package io.github.moh_mohsin.ahoyweatherapp.domain

import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository
import javax.inject.Inject

class CleanWeatherCacheUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend operator fun invoke() {
        weatherRepository.cleanCache()
    }
}