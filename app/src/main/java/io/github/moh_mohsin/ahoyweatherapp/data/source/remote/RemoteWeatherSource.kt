package io.github.moh_mohsin.ahoyweatherapp.data.source.remote

import io.github.moh_mohsin.ahoyweatherapp.BuildConfig
import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.source.WeatherSource
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api.OpenWeatherApi
import io.github.moh_mohsin.ahoyweatherapp.util.handle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

class RemoteWeatherSource(private val openWeatherApi: OpenWeatherApi) : WeatherSource {
    override suspend fun getWeather(lat: Double, lon: Double): StateFlow<Result<WeatherInfoDto>> {
        val result = openWeatherApi.oneCall(lat, lon, BuildConfig.OPEN_WEATHER_API_KEY).handle()
        return MutableStateFlow(result)
    }

    override suspend fun saveWeather(weatherInfoDto: WeatherInfoDto) {
        error("No Implementation available")
    }
}