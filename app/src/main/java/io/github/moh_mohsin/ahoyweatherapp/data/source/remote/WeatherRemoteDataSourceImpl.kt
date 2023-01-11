package io.github.moh_mohsin.ahoyweatherapp.data.source.remote

import io.github.moh_mohsin.ahoyweatherapp.BuildConfig
import io.github.moh_mohsin.ahoyweatherapp.data.NetworkException
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherRemoteDataSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.TempScale
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api.OpenWeatherApi
import io.github.moh_mohsin.ahoyweatherapp.util.handle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(private val openWeatherApi: OpenWeatherApi) :
    WeatherRemoteDataSource {
    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        tempScale: TempScale
    ): StateFlow<Result<WeatherInfoDto>> {
        val result = try {
            openWeatherApi.oneCall(lat, lon, BuildConfig.OPEN_WEATHER_API_KEY, tempScale.toString())
                .handle()
        } catch (e: Exception) {
            Result.Error(NetworkException())
        }
        return MutableStateFlow(result)
    }
}