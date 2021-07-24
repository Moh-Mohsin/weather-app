package io.github.moh_mohsin.ahoyweatherapp.data.source

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.TempScale
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto
import kotlinx.coroutines.flow.StateFlow

interface WeatherRemoteDataSource {

    suspend fun getWeather(
        lat: Double,
        lon: Double,
        tempScale: TempScale
    ): StateFlow<Result<WeatherInfoDto>>

}