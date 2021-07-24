package io.github.moh_mohsin.ahoyweatherapp.domain

import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.data.repository.CityRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteCitiesUseCase(private val cityRepository: CityRepository) {

    operator fun invoke(): Flow<List<City>> {
        return cityRepository.getFavorites()
    }
}