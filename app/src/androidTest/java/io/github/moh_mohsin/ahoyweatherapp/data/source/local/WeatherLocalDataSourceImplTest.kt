package io.github.moh_mohsin.ahoyweatherapp.data.source.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.moh_mohsin.ahoyweatherapp.data.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class WeatherLocalDataSourceImplTest {

    private val db = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext,
        AppDatabase::class.java
    ).build()
    private val weatherLocalDataSourceImpl = WeatherLocalDataSourceImpl(db)

    @Test
    fun should_return_weather_info_after_insertion() = runBlocking {
        val city = TestUtil.dummyCities().first()

        val savedWeatherInfoDto = weatherLocalDataSourceImpl.getWeather(city.lat, city.lng).first()
        assertEquals(null, savedWeatherInfoDto)
        val weatherInfoDto = TestUtil.makeWeatherInfoDto(city.lat, city.lng)

        weatherLocalDataSourceImpl.saveWeather(weatherInfoDto)
        val savedWeatherInfoDto1 = weatherLocalDataSourceImpl.getWeather(city.lat, city.lng).first()
        assertEquals(weatherInfoDto, savedWeatherInfoDto1)

    }

    @Test
    fun should_return_empty_after_cleaning_cache() = runBlocking {
        val weatherInfoDtos =
            TestUtil.dummyCities().map { TestUtil.makeWeatherInfoDto(it.lat, it.lng) }
        for (weatherInfoDto in weatherInfoDtos) {
            weatherLocalDataSourceImpl.saveWeather(weatherInfoDto)
            val savedWeatherInfoDto1 =
                weatherLocalDataSourceImpl.getWeather(weatherInfoDto.lat, weatherInfoDto.lon)
                    .first()
            assertEquals(weatherInfoDto, savedWeatherInfoDto1)
        }
        weatherLocalDataSourceImpl.cleanCache()

        for (weatherInfoDto in weatherInfoDtos) {
            val savedWeatherInfoDto1 =
                weatherLocalDataSourceImpl.getWeather(weatherInfoDto.lat, weatherInfoDto.lon)
                    .first()
            assertEquals(null, savedWeatherInfoDto1)
        }

    }

    @After
    fun tearDown() {
        db.close()
    }
}