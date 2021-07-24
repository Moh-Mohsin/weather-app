package io.github.moh_mohsin.ahoyweatherapp.data.repository

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.CityDto
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    suspend fun insertAll(citiesDto: List<CityDto>): Result<Unit>

    suspend fun searchCities(query: String): List<City>

    fun getFavorites(): Flow<List<City>>

    suspend fun addToFavorites(city: City)

    suspend fun removeFromFavorites(city: City)
}