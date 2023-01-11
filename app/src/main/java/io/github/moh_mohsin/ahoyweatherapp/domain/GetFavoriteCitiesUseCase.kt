package io.github.moh_mohsin.ahoyweatherapp.domain

import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.data.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(private val cityRepository: CityRepository) {

    operator fun invoke(): Flow<List<City>> {
        return cityRepository.getFavorites()
    }
}