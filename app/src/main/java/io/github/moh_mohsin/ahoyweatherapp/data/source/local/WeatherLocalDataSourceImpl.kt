package io.github.moh_mohsin.ahoyweatherapp.data.source.local

import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherLocalDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class WeatherLocalDataSourceImpl(val db: AppDatabase) : WeatherLocalDataSource {
    override fun getWeather(lat: Double, lon: Double): Flow<WeatherInfoDto?> {
        return db.weatherInfoDao().findByLatLon(lat, lon).map { res ->
//            val res = it?.toSuccess() ?: Result.Loading
            Timber.d("getWeather db: res")
            res
        }
    }

    override suspend fun saveWeather(weatherInfoDto: WeatherInfoDto) {
        db.weatherInfoDao().insertAll(weatherInfoDto)
    }

    override suspend fun cleanCache() {
        db.weatherInfoDao().deleteAll()
    }

}