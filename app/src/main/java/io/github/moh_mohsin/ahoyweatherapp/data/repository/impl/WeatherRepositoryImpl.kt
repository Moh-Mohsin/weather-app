package io.github.moh_mohsin.ahoyweatherapp.data.repository.impl

import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.map
import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.toWeatherInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber

class WeatherRepositoryImpl(
    private val weatherLocalDataSource: WeatherDataSource,
    private val weatherRemoteDataSource: WeatherDataSource
) : WeatherRepository {

    private val weatherStateFlow = MutableStateFlow<Result<WeatherInfo>>(Result.Loading)

    override suspend fun getWeather(lat: Double, lon: Double): StateFlow<Result<WeatherInfo>> {
        //TODO: use a different scope?
        coroutineScope {
            withContext(Dispatchers.IO) {
                updateWeather(lat, lon)
            }
        }
        return weatherStateFlow
    }

    private suspend fun updateWeather(lat: Double, lon: Double) {
        if (weatherStateFlow.value is Result.Error) {
            weatherStateFlow.value = Result.Loading
        }
        val remoteResult = weatherRemoteDataSource.getWeather(lat, lon).value.map { it.toWeatherInfo() }
        Timber.d("remoteResult: $remoteResult")
        if (weatherStateFlow.value !is Result.Success || remoteResult is Result.Success) {
            weatherStateFlow.value = remoteResult
        }
    }
}