package io.github.moh_mohsin.ahoyweatherapp.data

import io.github.moh_mohsin.ahoyweatherapp.BuildConfig
import io.github.moh_mohsin.ahoyweatherapp.data.model.*
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.*
import java.util.*


fun WeatherDto.toWeather() =
    Weather(id, main, description, "http://${BuildConfig.OPEN_WEATHER_DOMAIN}/img/wn/$icon@2x.png")

fun TempDto.toTemp() = Temp(day, min, max, night, eve, morn)

fun FeelsLikeDto.toFeelsLike() = FeelsLike(day, night, eve, morn)

fun DailyDto.toDaily() = Daily(
    Date(dt * 1000L),
    sunrise,
    sunset,
    moonrise,
    moonset,
    moonPhase,
    temp.toTemp(),
    feelsLike.toFeelsLike(),
    pressure,
    humidity,
    dewPoint,
    windSpeed,
    windDeg,
    windGust,
    weather.map { it.toWeather() },
    clouds,
    pop,
    uvi,
    rain
)

fun CurrentDto.toCurrent() = Current(
    Date(dt * 1000L),
    sunrise,
    sunset,
    temp,
    feelsLike,
    pressure,
    humidity,
    dewPoint,
    uvi,
    clouds,
    visibility,
    windSpeed,
    windDeg,
    windGust,
    weather.map { it.toWeather() },
)

fun WeatherInfoDto.toWeatherInfo() = WeatherInfo(lat, lon, timezone, timezoneOffset, current.toCurrent(), daily.map { it.toDaily() })