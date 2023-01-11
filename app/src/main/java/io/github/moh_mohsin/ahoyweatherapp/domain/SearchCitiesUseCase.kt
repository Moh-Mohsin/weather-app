package io.github.moh_mohsin.ahoyweatherapp.domain

import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.data.repository.CityRepository
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor(private val cityRepository: CityRepository) {

    suspend operator fun invoke(query: String): List<City> {
        return cityRepository.searchCities(query)
    }
}