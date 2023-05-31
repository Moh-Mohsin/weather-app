package io.github.moh_mohsin.ahoyweatherapp.data.repository.impl

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.data.repository.CityRepository
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.FavoriteCityDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.AppDatabase
import io.github.moh_mohsin.ahoyweatherapp.data.toCity
import io.github.moh_mohsin.ahoyweatherapp.data.toCityDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(private val db: AppDatabase) : CityRepository {
    override suspend fun insertAll(cities: List<City>): Result<Unit> {
        db.cityDao().insertAll(*cities.map { it.toCityDto() }.toTypedArray())
        return Result.Success(Unit)
    }

    override suspend fun searchCities(query: String): List<City> {
        return db.cityDao().findByNameOrCountry(query).map { it.toCity() }
    }

    override fun getFavorites(): Flow<List<City>> {
        return db.cityDao().getFavorite().map { cityDtos -> cityDtos.map { it.toCity() } }
    }

    override suspend fun addToFavorites(city: City) {
        Timber.d("add to favorites: ${city.name}")
        val favoriteCityDto = FavoriteCityDto(city.id)
        db.favoriteDao().insertAll(favoriteCityDto)
    }

    override suspend fun removeFromFavorites(city: City) {
        Timber.d("remove from favorites: ${city.name}")
        val favoriteCityDto = FavoriteCityDto(city.id)
        db.favoriteDao().delete(favoriteCityDto)
    }
}