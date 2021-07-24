package io.github.moh_mohsin.ahoyweatherapp.data.repository.impl

import io.github.moh_mohsin.ahoyweatherapp.data.*
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository
import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherLocalDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherRemoteDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.local.AppPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class WeatherRepositoryImpl(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val appPreference: AppPreference,
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : WeatherRepository {

    private val remoteWeatherStateFlow = MutableStateFlow<Result<WeatherInfoDto>>(Result.Loading)

    override fun getWeather(lat: Double, lon: Double): Flow<Result<WeatherInfo>> {

        when (val remoteWeather = remoteWeatherStateFlow.value) {
            is Result.Success -> {
                if (remoteWeather.data.lat == lat && remoteWeather.data.lon == lon) {
                    Timber.d("matching cache ($lat, $lon)")
                } else {
                    Timber.d("not matching cache ($lat, $lon), clearing(${remoteWeather.data.lat}, ${remoteWeather.data.lon})")
                    remoteWeatherStateFlow.value = Result.Loading
                }
            }
        }

        val res = weatherLocalDataSource.getWeather(lat, lon)
            .combine(remoteWeatherStateFlow) { localWeather, remoteWeatherResult ->
                val r = localWeather?.toSuccess() ?: remoteWeatherResult
                Timber.d(
                    "combine: ${
                        localWeather.toString().length
                    } + ${remoteWeatherResult.toString().length} = ${r.toString().length}"
                )
                r
            }.map { flow ->
                flow.map { result ->
//                    Timber.d("combine result: ${result.toString().length}")
                    result.toWeatherInfo()
                }
            }

        refreshWeather(lat, lon)
        return res
    }

    override suspend fun getWeatherOnline(lat: Double, lon: Double): Result<WeatherInfo> {
        return weatherRemoteDataSource.getWeather(
            lat,
            lon,
            appPreference.getTempScale()
        ).value.map { it.toWeatherInfo() }
    }

    private fun refreshWeather(lat: Double, lon: Double) {
        //TODO: use a different scope?
        CoroutineScope(coroutineContext).launch {
            if (remoteWeatherStateFlow.value is Result.Error) {
                remoteWeatherStateFlow.value = Result.Loading
            }
//            delay(1000)
            remoteWeatherStateFlow.value =
                weatherRemoteDataSource.getWeather(lat, lon, appPreference.getTempScale()).value
            remoteWeatherStateFlow.value.getOrNull()?.let {
                weatherLocalDataSource.saveWeather(it)
            }
            Timber.d("remoteResult: ${remoteWeatherStateFlow.value}")
        }
    }

    override suspend fun cleanCache() {
        weatherLocalDataSource.cleanCache()
    }
}

