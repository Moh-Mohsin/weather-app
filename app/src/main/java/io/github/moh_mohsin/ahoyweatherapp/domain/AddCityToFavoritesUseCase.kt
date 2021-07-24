package io.github.moh_mohsin.ahoyweatherapp.domain

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.repository.CityRepository
import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.round

class AddCityToFavoritesUseCase(private val cityRepository: CityRepository) {

    suspend operator fun invoke(city: City) {
        cityRepository.addToFavorites(city)
    }
}