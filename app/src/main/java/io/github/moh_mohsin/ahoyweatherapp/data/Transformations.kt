package io.github.moh_mohsin.ahoyweatherapp.data

import io.github.moh_mohsin.ahoyweatherapp.BuildConfig
import io.github.moh_mohsin.ahoyweatherapp.data.model.*
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.*
import java.util.*


fun WeatherDescDto.toWeatherDesc() =
    WeatherDesc(id, main, description, "http://${BuildConfig.OPEN_WEATHER_DOMAIN}/img/wn/$icon@2x.png")

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
    weather.map { it.toWeatherDesc() },
    clouds,
    pop,
    uvi,
    rain
)

fun WeatherDetailDto.toWeatherDetail() = WeatherDetail(
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
    weather.map { it.toWeatherDesc() },
)

fun WeatherInfoDto.toWeatherInfo(tempScale: TempScale) = WeatherInfo(
    lat,
    lon,
    timezone,
    timezoneOffset,
    current.toWeatherDetail(),
    daily.map { it.toDaily() },
    hourly.map { it.toWeatherDetail() },
    tempScale = tempScale
)

fun CityDto.toCity() = City(id, city, cityAscii, lat, lng, country, iso2, iso3, adminName, capital)
fun City.toCityDto() = CityDto(id, name, nameAscii, lat, lng, country, iso2, iso3, adminName, capital)