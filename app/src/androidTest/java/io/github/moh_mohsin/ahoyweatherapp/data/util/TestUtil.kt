package io.github.moh_mohsin.ahoyweatherapp.data.util

import io.github.moh_mohsin.ahoyweatherapp.data.model.City
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherDetailDto
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto

object TestUtil {

    fun dummyCities() = listOf(
        City(
            1392685764,
            "Tokyo",
            "Tokyo",
            35.6897,
            139.6922,
            "Japan",
            "JP",
            "JPN",
            "Tōkyō",
            "primary"
        ),
        City(
            1360771077,
            "Jakarta",
            "Jakarta",
            -6.2146,
            106.8451,
            "Indonesia",
            "ID",
            "IDN",
            "Jakarta",
            "primary"
        ),
        City(
            1356872604,
            "Delhi",
            "Delhi",
            28.66,
            77.23,
            "India",
            "IN",
            "IND",
            "Delhi",
            "admin"
        ),
    )

    fun makeWeatherInfoDto(lat: Double, lon: Double) = WeatherInfoDto(
        lat,
        lon,
        "",
        1,
        WeatherDetailDto(1, 1, 1, 1.0, 1.0, 1, 1, 1.0, 1.0, 1, 1, 1.0, 1, 1.0, listOf()),
        listOf(),
        listOf()
    )
}
