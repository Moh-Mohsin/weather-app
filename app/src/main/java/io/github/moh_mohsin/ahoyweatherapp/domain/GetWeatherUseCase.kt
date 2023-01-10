package io.github.moh_mohsin.ahoyweatherapp.domain

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.model.WeatherInfo
import io.github.moh_mohsin.ahoyweatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlin.math.round

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {

    operator fun invoke(lat: Double, lon: Double): Flow<Result<WeatherInfo>> {
        // OpenWeather API ignores decimal points past 4 digits in lat/lon,
        // so we round it to that so we can use the value in our Room queries later
        return weatherRepository.getWeather(lat.round(4), lon.round(4))
    }
}
fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}