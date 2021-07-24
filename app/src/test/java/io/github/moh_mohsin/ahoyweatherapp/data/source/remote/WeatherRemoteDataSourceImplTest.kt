package io.github.moh_mohsin.ahoyweatherapp.data.source.remote

import io.github.moh_mohsin.ahoyweatherapp.data.Result
import io.github.moh_mohsin.ahoyweatherapp.data.require
import io.github.moh_mohsin.ahoyweatherapp.data.source.dto.TempScale
import io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api.OpenWeatherService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import java.net.HttpURLConnection
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WeatherRemoteDataSourceImplTest {
    private val mockWebServer = MockWebServer().also {
        it.start()
    }

    private val mockServerService = OpenWeatherService().getService(mockWebServer.url("/data/2.5/"))
    private val weatherRemoteDataSourceImpl = WeatherRemoteDataSourceImpl(mockServerService)


    @Test
    fun `should return weather info when given lat and lon`() = runBlocking {
        val response = MockResponse().apply {
            setResponseCode(HttpURLConnection.HTTP_CREATED)
            setBody(response)
        }
        mockWebServer.enqueue(response)
        val result = weatherRemoteDataSourceImpl.getWeather(lat, lon, TempScale.METRIC).value

        val request = mockWebServer.takeRequest()
        assertTrue { request.path?.startsWith("/data/2.5/onecall") ?: false }
        assertTrue { result is Result.Success }
        assertEquals(lat, result.require().lat)
        assertEquals(lon, result.require().lon)
    }

    @After
    fun cleanUp() {
        mockWebServer.shutdown()
    }
}

const val lat = 35.6897
const val lon = 139.6922
val response = """
    {
        "lat": $lat,
        "lon": $lon,
        "timezone": "Asia/Tokyo",
        "timezone_offset": 32400,
        "current": {
            "dt": 1627144443,
            "sunrise": 1627155810,
            "sunset": 1627206701,
            "temp": 26.68,
            "feels_like": 28.9,
            "pressure": 1010,
            "humidity": 78,
            "dew_point": 22.52,
            "uvi": 0,
            "clouds": 20,
            "visibility": 10000,
            "wind_speed": 0.45,
            "wind_deg": 114,
            "wind_gust": 0.89,
            "weather": [
                {
                    "id": 801,
                    "main": "Clouds",
                    "description": "few clouds",
                    "icon": "02n"
                }
            ]
        },
        "hourly": [
            {
                "dt": 1627142400,
                "temp": 26.63,
                "feels_like": 26.63,
                "pressure": 1010,
                "humidity": 76,
                "dew_point": 22.05,
                "uvi": 0,
                "clouds": 20,
                "visibility": 10000,
                "wind_speed": 2.5,
                "wind_deg": 123,
                "wind_gust": 2.49,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02n"
                    }
                ],
                "pop": 0.1
            },
            {
                "dt": 1627146000,
                "temp": 26.68,
                "feels_like": 28.9,
                "pressure": 1010,
                "humidity": 78,
                "dew_point": 22.52,
                "uvi": 0,
                "clouds": 20,
                "visibility": 10000,
                "wind_speed": 2.35,
                "wind_deg": 107,
                "wind_gust": 2.42,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02n"
                    }
                ],
                "pop": 0.1
            },
            {
                "dt": 1627149600,
                "temp": 26.55,
                "feels_like": 26.55,
                "pressure": 1010,
                "humidity": 76,
                "dew_point": 21.97,
                "uvi": 0,
                "clouds": 19,
                "visibility": 10000,
                "wind_speed": 2.21,
                "wind_deg": 89,
                "wind_gust": 2.29,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02n"
                    }
                ],
                "pop": 0.1
            },
            {
                "dt": 1627153200,
                "temp": 26.35,
                "feels_like": 26.35,
                "pressure": 1010,
                "humidity": 75,
                "dew_point": 21.56,
                "uvi": 0,
                "clouds": 12,
                "visibility": 10000,
                "wind_speed": 2.3,
                "wind_deg": 75,
                "wind_gust": 2.41,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02n"
                    }
                ],
                "pop": 0.01
            },
            {
                "dt": 1627156800,
                "temp": 26.1,
                "feels_like": 26.1,
                "pressure": 1011,
                "humidity": 74,
                "dew_point": 21.1,
                "uvi": 0,
                "clouds": 8,
                "visibility": 10000,
                "wind_speed": 2.15,
                "wind_deg": 59,
                "wind_gust": 2.26,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627160400,
                "temp": 26.08,
                "feels_like": 26.08,
                "pressure": 1011,
                "humidity": 72,
                "dew_point": 20.64,
                "uvi": 0.44,
                "clouds": 4,
                "visibility": 10000,
                "wind_speed": 2.52,
                "wind_deg": 57,
                "wind_gust": 2.63,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627164000,
                "temp": 26.52,
                "feels_like": 26.52,
                "pressure": 1011,
                "humidity": 67,
                "dew_point": 20.11,
                "uvi": 1.25,
                "clouds": 1,
                "visibility": 10000,
                "wind_speed": 2.76,
                "wind_deg": 57,
                "wind_gust": 2.82,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627167600,
                "temp": 27.39,
                "feels_like": 28.77,
                "pressure": 1011,
                "humidity": 62,
                "dew_point": 19.62,
                "uvi": 2.73,
                "clouds": 1,
                "visibility": 10000,
                "wind_speed": 2.99,
                "wind_deg": 56,
                "wind_gust": 3.13,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627171200,
                "temp": 28.44,
                "feels_like": 29.74,
                "pressure": 1011,
                "humidity": 57,
                "dew_point": 19.05,
                "uvi": 4.59,
                "clouds": 1,
                "visibility": 10000,
                "wind_speed": 3,
                "wind_deg": 53,
                "wind_gust": 3.3,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627174800,
                "temp": 29.6,
                "feels_like": 30.62,
                "pressure": 1010,
                "humidity": 51,
                "dew_point": 18.51,
                "uvi": 7.41,
                "clouds": 10,
                "visibility": 10000,
                "wind_speed": 2.87,
                "wind_deg": 52,
                "wind_gust": 3.22,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627178400,
                "temp": 30.76,
                "feels_like": 31.52,
                "pressure": 1010,
                "humidity": 46,
                "dew_point": 17.81,
                "uvi": 8.8,
                "clouds": 10,
                "visibility": 10000,
                "wind_speed": 2.6,
                "wind_deg": 53,
                "wind_gust": 2.96,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627182000,
                "temp": 31.87,
                "feels_like": 32.27,
                "pressure": 1009,
                "humidity": 41,
                "dew_point": 16.98,
                "uvi": 9.04,
                "clouds": 7,
                "visibility": 10000,
                "wind_speed": 2.62,
                "wind_deg": 56,
                "wind_gust": 3.07,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627185600,
                "temp": 32.99,
                "feels_like": 33.16,
                "pressure": 1008,
                "humidity": 37,
                "dew_point": 16.3,
                "uvi": 8.4,
                "clouds": 6,
                "visibility": 10000,
                "wind_speed": 2.53,
                "wind_deg": 61,
                "wind_gust": 2.99,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627189200,
                "temp": 33.82,
                "feels_like": 33.78,
                "pressure": 1008,
                "humidity": 34,
                "dew_point": 15.7,
                "uvi": 6.46,
                "clouds": 4,
                "visibility": 10000,
                "wind_speed": 3.01,
                "wind_deg": 85,
                "wind_gust": 3.22,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627192800,
                "temp": 33.74,
                "feels_like": 34.75,
                "pressure": 1007,
                "humidity": 39,
                "dew_point": 17.23,
                "uvi": 4.15,
                "clouds": 5,
                "visibility": 10000,
                "wind_speed": 5.43,
                "wind_deg": 116,
                "wind_gust": 4.7,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627196400,
                "temp": 33.05,
                "feels_like": 34.54,
                "pressure": 1007,
                "humidity": 43,
                "dew_point": 18.57,
                "uvi": 1.75,
                "clouds": 15,
                "visibility": 10000,
                "wind_speed": 6.66,
                "wind_deg": 110,
                "wind_gust": 5.5,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627200000,
                "temp": 31.97,
                "feels_like": 33.62,
                "pressure": 1007,
                "humidity": 47,
                "dew_point": 19.08,
                "uvi": 0.63,
                "clouds": 13,
                "visibility": 10000,
                "wind_speed": 6.4,
                "wind_deg": 107,
                "wind_gust": 5.35,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627203600,
                "temp": 30.95,
                "feels_like": 32.53,
                "pressure": 1008,
                "humidity": 50,
                "dew_point": 19.34,
                "uvi": 0.14,
                "clouds": 12,
                "visibility": 10000,
                "wind_speed": 6.15,
                "wind_deg": 108,
                "wind_gust": 5.36,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627207200,
                "temp": 29.63,
                "feels_like": 31.12,
                "pressure": 1008,
                "humidity": 54,
                "dew_point": 19.49,
                "uvi": 0,
                "clouds": 15,
                "visibility": 10000,
                "wind_speed": 5.74,
                "wind_deg": 106,
                "wind_gust": 5.61,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627210800,
                "temp": 28.67,
                "feels_like": 30.08,
                "pressure": 1009,
                "humidity": 57,
                "dew_point": 19.25,
                "uvi": 0,
                "clouds": 17,
                "visibility": 10000,
                "wind_speed": 5.07,
                "wind_deg": 102,
                "wind_gust": 5.07,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627214400,
                "temp": 27.9,
                "feels_like": 29.2,
                "pressure": 1009,
                "humidity": 59,
                "dew_point": 19.14,
                "uvi": 0,
                "clouds": 17,
                "visibility": 10000,
                "wind_speed": 4.59,
                "wind_deg": 93,
                "wind_gust": 4.67,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627218000,
                "temp": 27.28,
                "feels_like": 28.61,
                "pressure": 1010,
                "humidity": 62,
                "dew_point": 19.29,
                "uvi": 0,
                "clouds": 18,
                "visibility": 10000,
                "wind_speed": 4.16,
                "wind_deg": 79,
                "wind_gust": 4.34,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02n"
                    }
                ],
                "pop": 0.05
            },
            {
                "dt": 1627221600,
                "temp": 26.82,
                "feels_like": 28.13,
                "pressure": 1009,
                "humidity": 64,
                "dew_point": 19.44,
                "uvi": 0,
                "clouds": 25,
                "visibility": 10000,
                "wind_speed": 4.16,
                "wind_deg": 67,
                "wind_gust": 4.45,
                "weather": [
                    {
                        "id": 802,
                        "main": "Clouds",
                        "description": "scattered clouds",
                        "icon": "03n"
                    }
                ],
                "pop": 0.01
            },
            {
                "dt": 1627225200,
                "temp": 26.49,
                "feels_like": 26.49,
                "pressure": 1009,
                "humidity": 64,
                "dew_point": 19.27,
                "uvi": 0,
                "clouds": 25,
                "visibility": 10000,
                "wind_speed": 4.11,
                "wind_deg": 55,
                "wind_gust": 4.44,
                "weather": [
                    {
                        "id": 802,
                        "main": "Clouds",
                        "description": "scattered clouds",
                        "icon": "03n"
                    }
                ],
                "pop": 0.01
            },
            {
                "dt": 1627228800,
                "temp": 26.18,
                "feels_like": 26.18,
                "pressure": 1008,
                "humidity": 65,
                "dew_point": 19.09,
                "uvi": 0,
                "clouds": 24,
                "visibility": 10000,
                "wind_speed": 4.29,
                "wind_deg": 49,
                "wind_gust": 4.77,
                "weather": [
                    {
                        "id": 801,
                        "main": "Clouds",
                        "description": "few clouds",
                        "icon": "02n"
                    }
                ],
                "pop": 0.01
            },
            {
                "dt": 1627232400,
                "temp": 25.84,
                "feels_like": 26.2,
                "pressure": 1008,
                "humidity": 66,
                "dew_point": 19.15,
                "uvi": 0,
                "clouds": 27,
                "visibility": 10000,
                "wind_speed": 4.56,
                "wind_deg": 48,
                "wind_gust": 5.25,
                "weather": [
                    {
                        "id": 802,
                        "main": "Clouds",
                        "description": "scattered clouds",
                        "icon": "03n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627236000,
                "temp": 25.64,
                "feels_like": 26.01,
                "pressure": 1008,
                "humidity": 67,
                "dew_point": 19.18,
                "uvi": 0,
                "clouds": 35,
                "visibility": 10000,
                "wind_speed": 4.34,
                "wind_deg": 48,
                "wind_gust": 5.2,
                "weather": [
                    {
                        "id": 802,
                        "main": "Clouds",
                        "description": "scattered clouds",
                        "icon": "03n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627239600,
                "temp": 25.54,
                "feels_like": 25.9,
                "pressure": 1007,
                "humidity": 67,
                "dew_point": 19.04,
                "uvi": 0,
                "clouds": 94,
                "visibility": 10000,
                "wind_speed": 4.05,
                "wind_deg": 45,
                "wind_gust": 4.83,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627243200,
                "temp": 25.42,
                "feels_like": 25.77,
                "pressure": 1007,
                "humidity": 67,
                "dew_point": 18.95,
                "uvi": 0,
                "clouds": 97,
                "visibility": 10000,
                "wind_speed": 4.25,
                "wind_deg": 51,
                "wind_gust": 5,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627246800,
                "temp": 25.1,
                "feels_like": 25.49,
                "pressure": 1007,
                "humidity": 70,
                "dew_point": 19.26,
                "uvi": 0.12,
                "clouds": 98,
                "visibility": 10000,
                "wind_speed": 4.69,
                "wind_deg": 59,
                "wind_gust": 5.43,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627250400,
                "temp": 24.89,
                "feels_like": 25.29,
                "pressure": 1008,
                "humidity": 71,
                "dew_point": 19.36,
                "uvi": 0.53,
                "clouds": 98,
                "visibility": 10000,
                "wind_speed": 4.67,
                "wind_deg": 57,
                "wind_gust": 5.71,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627254000,
                "temp": 25.23,
                "feels_like": 25.61,
                "pressure": 1007,
                "humidity": 69,
                "dew_point": 19.18,
                "uvi": 1.17,
                "clouds": 99,
                "visibility": 10000,
                "wind_speed": 4.27,
                "wind_deg": 44,
                "wind_gust": 5.22,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627257600,
                "temp": 26.52,
                "feels_like": 26.52,
                "pressure": 1007,
                "humidity": 63,
                "dew_point": 18.93,
                "uvi": 1.97,
                "clouds": 99,
                "visibility": 10000,
                "wind_speed": 4.15,
                "wind_deg": 27,
                "wind_gust": 5.16,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627261200,
                "temp": 28.07,
                "feels_like": 29.22,
                "pressure": 1006,
                "humidity": 57,
                "dew_point": 18.93,
                "uvi": 5.96,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 4.17,
                "wind_deg": 22,
                "wind_gust": 5.42,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627264800,
                "temp": 29.48,
                "feels_like": 30.44,
                "pressure": 1005,
                "humidity": 51,
                "dew_point": 18.42,
                "uvi": 7.07,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 3.86,
                "wind_deg": 19,
                "wind_gust": 5.33,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627268400,
                "temp": 30.8,
                "feels_like": 31.41,
                "pressure": 1004,
                "humidity": 45,
                "dew_point": 17.31,
                "uvi": 7.27,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 3.75,
                "wind_deg": 29,
                "wind_gust": 5.19,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627272000,
                "temp": 31.31,
                "feels_like": 31.48,
                "pressure": 1004,
                "humidity": 41,
                "dew_point": 16.39,
                "uvi": 7.28,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 4.15,
                "wind_deg": 52,
                "wind_gust": 5.41,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627275600,
                "temp": 31.74,
                "feels_like": 31.58,
                "pressure": 1003,
                "humidity": 38,
                "dew_point": 15.58,
                "uvi": 5.58,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 4.48,
                "wind_deg": 67,
                "wind_gust": 5.41,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627279200,
                "temp": 31.68,
                "feels_like": 31.5,
                "pressure": 1002,
                "humidity": 38,
                "dew_point": 15.28,
                "uvi": 3.58,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 5.17,
                "wind_deg": 77,
                "wind_gust": 5.64,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627282800,
                "temp": 30.7,
                "feels_like": 30.53,
                "pressure": 1002,
                "humidity": 40,
                "dew_point": 15.58,
                "uvi": 1.66,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 6.58,
                "wind_deg": 84,
                "wind_gust": 6.07,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627286400,
                "temp": 29.36,
                "feels_like": 29.4,
                "pressure": 1002,
                "humidity": 44,
                "dew_point": 15.87,
                "uvi": 0.6,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 5.97,
                "wind_deg": 81,
                "wind_gust": 5.69,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627290000,
                "temp": 28.17,
                "feels_like": 28.46,
                "pressure": 1002,
                "humidity": 48,
                "dew_point": 16.05,
                "uvi": 0.13,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 6.09,
                "wind_deg": 83,
                "wind_gust": 6.29,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627293600,
                "temp": 27.08,
                "feels_like": 27.62,
                "pressure": 1003,
                "humidity": 52,
                "dew_point": 16.38,
                "uvi": 0,
                "clouds": 98,
                "visibility": 10000,
                "wind_speed": 5.09,
                "wind_deg": 87,
                "wind_gust": 5.54,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627297200,
                "temp": 26.3,
                "feels_like": 26.3,
                "pressure": 1003,
                "humidity": 56,
                "dew_point": 16.96,
                "uvi": 0,
                "clouds": 96,
                "visibility": 10000,
                "wind_speed": 4.27,
                "wind_deg": 88,
                "wind_gust": 4.58,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627300800,
                "temp": 25.93,
                "feels_like": 26.12,
                "pressure": 1003,
                "humidity": 59,
                "dew_point": 17.16,
                "uvi": 0,
                "clouds": 96,
                "visibility": 10000,
                "wind_speed": 3.74,
                "wind_deg": 87,
                "wind_gust": 3.84,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627304400,
                "temp": 25.52,
                "feels_like": 25.72,
                "pressure": 1003,
                "humidity": 61,
                "dew_point": 17.57,
                "uvi": 0,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 3.5,
                "wind_deg": 98,
                "wind_gust": 3.46,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627308000,
                "temp": 25.27,
                "feels_like": 25.47,
                "pressure": 1003,
                "humidity": 62,
                "dew_point": 17.59,
                "uvi": 0,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 1.54,
                "wind_deg": 121,
                "wind_gust": 1.42,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04n"
                    }
                ],
                "pop": 0
            },
            {
                "dt": 1627311600,
                "temp": 25.39,
                "feels_like": 25.6,
                "pressure": 1002,
                "humidity": 62,
                "dew_point": 17.68,
                "uvi": 0,
                "clouds": 100,
                "visibility": 10000,
                "wind_speed": 2.12,
                "wind_deg": 119,
                "wind_gust": 1.83,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04n"
                    }
                ],
                "pop": 0
            }
        ],
        "daily": [
            {
                "dt": 1627178400,
                "sunrise": 1627155810,
                "sunset": 1627206701,
                "moonrise": 1627211100,
                "moonset": 1627158420,
                "moon_phase": 0.54,
                "temp": {
                    "day": 30.76,
                    "min": 26.08,
                    "max": 33.82,
                    "night": 26.82,
                    "eve": 31.97,
                    "morn": 26.1
                },
                "feels_like": {
                    "day": 31.52,
                    "night": 28.13,
                    "eve": 33.62,
                    "morn": 26.1
                },
                "pressure": 1010,
                "humidity": 46,
                "dew_point": 17.81,
                "wind_speed": 6.66,
                "wind_deg": 110,
                "wind_gust": 5.61,
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "clouds": 10,
                "pop": 0.1,
                "uvi": 9.04
            },
            {
                "dt": 1627264800,
                "sunrise": 1627242255,
                "sunset": 1627293058,
                "moonrise": 1627299660,
                "moonset": 1627248960,
                "moon_phase": 0.57,
                "temp": {
                    "day": 29.48,
                    "min": 24.89,
                    "max": 31.74,
                    "night": 25.27,
                    "eve": 29.36,
                    "morn": 25.42
                },
                "feels_like": {
                    "day": 30.44,
                    "night": 25.47,
                    "eve": 29.4,
                    "morn": 25.77
                },
                "pressure": 1005,
                "humidity": 51,
                "dew_point": 18.42,
                "wind_speed": 6.58,
                "wind_deg": 84,
                "wind_gust": 6.29,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "clouds": 100,
                "pop": 0.01,
                "uvi": 7.28
            },
            {
                "dt": 1627351200,
                "sunrise": 1627328700,
                "sunset": 1627379413,
                "moonrise": 1627387920,
                "moonset": 1627339380,
                "moon_phase": 0.61,
                "temp": {
                    "day": 29.2,
                    "min": 25.37,
                    "max": 32.22,
                    "night": 26.79,
                    "eve": 30.1,
                    "morn": 25.98
                },
                "feels_like": {
                    "day": 30.05,
                    "night": 28.65,
                    "eve": 32.62,
                    "morn": 25.98
                },
                "pressure": 998,
                "humidity": 51,
                "dew_point": 17.9,
                "wind_speed": 6.87,
                "wind_deg": 174,
                "wind_gust": 6.02,
                "weather": [
                    {
                        "id": 500,
                        "main": "Rain",
                        "description": "light rain",
                        "icon": "10d"
                    }
                ],
                "clouds": 98,
                "pop": 0.52,
                "rain": 1.73,
                "uvi": 3.93
            },
            {
                "dt": 1627437600,
                "sunrise": 1627415146,
                "sunset": 1627465767,
                "moonrise": 1627476000,
                "moonset": 1627429500,
                "moon_phase": 0.64,
                "temp": {
                    "day": 31.51,
                    "min": 25.69,
                    "max": 32.48,
                    "night": 29.21,
                    "eve": 30.81,
                    "morn": 25.89
                },
                "feels_like": {
                    "day": 32.1,
                    "night": 31.73,
                    "eve": 32.69,
                    "morn": 26.41
                },
                "pressure": 999,
                "humidity": 43,
                "dew_point": 17.43,
                "wind_speed": 4.72,
                "wind_deg": 175,
                "wind_gust": 4.06,
                "weather": [
                    {
                        "id": 500,
                        "main": "Rain",
                        "description": "light rain",
                        "icon": "10d"
                    }
                ],
                "clouds": 94,
                "pop": 0.54,
                "rain": 0.93,
                "uvi": 6.47
            },
            {
                "dt": 1627524000,
                "sunrise": 1627501593,
                "sunset": 1627552119,
                "moonrise": 1627563960,
                "moonset": 1627519560,
                "moon_phase": 0.67,
                "temp": {
                    "day": 32.2,
                    "min": 27.06,
                    "max": 33.05,
                    "night": 29.32,
                    "eve": 30.57,
                    "morn": 27.06
                },
                "feels_like": {
                    "day": 34.49,
                    "night": 32.28,
                    "eve": 33.51,
                    "morn": 29.22
                },
                "pressure": 999,
                "humidity": 49,
                "dew_point": 20.09,
                "wind_speed": 5.53,
                "wind_deg": 177,
                "wind_gust": 5.36,
                "weather": [
                    {
                        "id": 500,
                        "main": "Rain",
                        "description": "light rain",
                        "icon": "10d"
                    }
                ],
                "clouds": 13,
                "pop": 0.67,
                "rain": 3.4,
                "uvi": 7
            },
            {
                "dt": 1627610400,
                "sunrise": 1627588039,
                "sunset": 1627638470,
                "moonrise": 1627651920,
                "moonset": 1627609440,
                "moon_phase": 0.71,
                "temp": {
                    "day": 32.75,
                    "min": 27.78,
                    "max": 32.75,
                    "night": 29.25,
                    "eve": 30.9,
                    "morn": 27.78
                },
                "feels_like": {
                    "day": 35.52,
                    "night": 32.33,
                    "eve": 34.16,
                    "morn": 30.49
                },
                "pressure": 1000,
                "humidity": 49,
                "dew_point": 20.65,
                "wind_speed": 4.97,
                "wind_deg": 169,
                "wind_gust": 5.36,
                "weather": [
                    {
                        "id": 500,
                        "main": "Rain",
                        "description": "light rain",
                        "icon": "10d"
                    }
                ],
                "clouds": 36,
                "pop": 0.44,
                "rain": 0.86,
                "uvi": 7
            },
            {
                "dt": 1627696800,
                "sunrise": 1627674486,
                "sunset": 1627724820,
                "moonrise": 1627739880,
                "moonset": 1627699320,
                "moon_phase": 0.75,
                "temp": {
                    "day": 32.93,
                    "min": 27.99,
                    "max": 32.93,
                    "night": 29.18,
                    "eve": 30.68,
                    "morn": 27.99
                },
                "feels_like": {
                    "day": 35.87,
                    "night": 32.55,
                    "eve": 34.41,
                    "morn": 30.52
                },
                "pressure": 1003,
                "humidity": 49,
                "dew_point": 20.76,
                "wind_speed": 6.1,
                "wind_deg": 157,
                "wind_gust": 5.72,
                "weather": [
                    {
                        "id": 804,
                        "main": "Clouds",
                        "description": "overcast clouds",
                        "icon": "04d"
                    }
                ],
                "clouds": 88,
                "pop": 0.04,
                "uvi": 7
            },
            {
                "dt": 1627783200,
                "sunrise": 1627760933,
                "sunset": 1627811168,
                "moonrise": 1627828020,
                "moonset": 1627789140,
                "moon_phase": 0.77,
                "temp": {
                    "day": 33.21,
                    "min": 27.24,
                    "max": 34.06,
                    "night": 28.7,
                    "eve": 31.77,
                    "morn": 27.24
                },
                "feels_like": {
                    "day": 35.32,
                    "night": 32.08,
                    "eve": 34.66,
                    "morn": 29.56
                },
                "pressure": 1005,
                "humidity": 45,
                "dew_point": 19.41,
                "wind_speed": 6.72,
                "wind_deg": 129,
                "wind_gust": 5.57,
                "weather": [
                    {
                        "id": 802,
                        "main": "Clouds",
                        "description": "scattered clouds",
                        "icon": "03d"
                    }
                ],
                "clouds": 36,
                "pop": 0.03,
                "uvi": 7
            }
        ]
    }
""".trimIndent()