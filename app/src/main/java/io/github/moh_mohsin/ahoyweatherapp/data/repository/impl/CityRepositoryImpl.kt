package io.github.moh_mohsin.ahoyweatherapp.data.repository.impl

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.data.repository.CityRepository
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.CityDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.FavoriteCityDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.AppDatabase
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.CityDao
import io.github.moh_mohsin.ahoyweatherapp.data.toCity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CityRepositoryImpl(private val db: AppDatabase) : CityRepository {
    override suspend fun insertAll(citiesDto: List<CityDto>): Result<Unit> {
        db.cityDao().insertAll(*citiesDto.toTypedArray())
        return Result.Success(Unit)
    }

    override suspend fun searchCities(query: String): List<City> {
        return db.cityDao().findByNameOrCountry(query).map { it.toCity() }
    }

    override fun getFavorites(): Flow<List<City>> {
        return db.cityDao().getFavorite().map { cityDtos -> cityDtos.map { it.toCity() } }
    }

    override suspend fun addToFavorites(city: City) {
        val favoriteCityDto = FavoriteCityDto(city.id)
        db.favoriteDao().insertAll(favoriteCityDto)
    }

    override suspend fun removeFromFavorites(city: City) {
        val favoriteCityDto = FavoriteCityDto(city.id)
        db.favoriteDao().delete(favoriteCityDto)
    }
}