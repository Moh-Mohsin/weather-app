package io.github.moh_mohsin.ahoyweatherapp.domain

import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.data.repository.CityRepository
import javax.inject.Inject

class AddCityToFavoritesUseCase @Inject constructor(private val cityRepository: CityRepository) {

    suspend operator fun invoke(city: City) {
        cityRepository.addToFavorites(city)
    }
}