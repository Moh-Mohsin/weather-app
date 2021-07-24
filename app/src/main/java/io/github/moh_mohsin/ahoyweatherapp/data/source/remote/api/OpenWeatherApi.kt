package io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api

import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.WeatherInfoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("onecall")
    suspend fun oneCall(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("appid")
        appid: String,
        @Query("units")
        units: String,
        @Query("exclude")
        exclude: String = "minutely",
    ): Response<WeatherInfoDto>
}